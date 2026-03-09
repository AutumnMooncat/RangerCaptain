package RangerCaptain.patches;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.UpgradeChangesPortraitPatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.ui.MultiUpgradeTree;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import javassist.CtBehavior;

import java.util.ArrayList;

public class SCVPatches {
    public static final PreviewScreen screen = new PreviewScreen();
    public static final CardGroup cardsToRender = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public static boolean viewingFusions = false;
    public static boolean shouldOpen = false;

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "close")
    @SpirePatch2(clz = SingleCardViewPopup.class, method = "updateBetaArtToggler")
    @SpirePatch2(clz = UpgradeChangesPortraitPatch.ToggleUpgrade.class, method = "Insert")
    public static class DontDispose {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> plz(AbstractCard ___card) {
            if (___card instanceof AbstractEasyCard && ((AbstractEasyCard) ___card).transientArt()) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(Texture.class, "dispose");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "update")
    public static class UpdateTime {
        @SpirePrefixPatch
        public static void plz(SingleCardViewPopup __instance, AbstractCard ___card) {
            if (viewingFusions) {
                screen.update();
            } else if (shouldOpen) {
                shouldOpen = false;
                viewingFusions = true;
                cardsToRender.clear();
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                if (CardCrawlGame.isInARun() && AbstractDungeon.player != null && AbstractDungeon.player.masterDeck.group.stream().anyMatch(Wiz::canBeFused)) {
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                        if (c instanceof AbstractEasyCard && Wiz.canBeFused(c) && temp.group.stream().noneMatch(card -> card instanceof AbstractEasyCard && ((AbstractEasyCard) card).getMonsterData() == ((AbstractEasyCard) c).getMonsterData())) {
                            temp.group.add(c);
                        }
                    }
                } else {
                    CardLibrary.getAllCards().forEach(c -> {
                        if (/*!Objects.equals(c.cardID, ___card.cardID) &&*/ Wiz.canBeFused(c)) {
                            temp.addToTop(c);
                        }
                    });
                }
                temp.sortAlphabetically(true);
                temp.sortByRarity(true);
                //cardsToRender.group.add(Wiz.fuse(___card, ___card));
                temp.group.forEach(c -> cardsToRender.group.add(Wiz.fuse(___card, c)));
                screen.calculateScrollBounds();
                screen.justSorted = true;
            }
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "render")
    public static class RenderTime {
        @SpirePostfixPatch
        public static void plz(SingleCardViewPopup __instance, SpriteBatch sb) {
            if (viewingFusions) {
                screen.render(sb);
            }
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "close")
    public static class ClosePatch {
        @SpirePostfixPatch
        public static void closeTime(SingleCardViewPopup __instance) {
            viewingFusions = false;
            cardsToRender.clear();
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "updateInput")
    public static class StopClosing {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> plz() {
            for (ClickableTipsPatch.TipClickable tp : ClickableTipsPatch.clickables) {
                if (tp.hb.hovered) {
                    return SpireReturn.Return();
                }
            }
            if (viewingFusions) {
                viewingFusions = false;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(SingleCardViewPopup.class, "betaArtHb");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "renderTips")
    public static class TipsBeGone {
        @SpirePrefixPatch
        public static SpireReturn<?> stop(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            return viewingFusions ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = MultiUpgradeTree.class, method = "update")
    @SpirePatch2(clz = MultiUpgradeTree.class, method = "render")
    public static class ClobberTime {
        @SpirePrefixPatch
        public static SpireReturn<?> ceaseAndDesist() {
            return CardAugments.patches.SCVPatches.viewingAugments ? SpireReturn.Return() : SpireReturn.Continue();// 144 145 147
        }
    }

    public static class PreviewScreen implements ScrollBarListener {
        private static final int CARDS_PER_LINE = (int)((float)Settings.WIDTH / (AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X * 3.0F));
        private static float drawStartX;
        private static final float drawStartY = (float)Settings.HEIGHT * 0.8F;
        private static final float padX = AbstractCard.IMG_WIDTH * 0.75F + Settings.CARD_VIEW_PAD_X;
        private static final float padY = AbstractCard.IMG_HEIGHT * 0.75F + Settings.CARD_VIEW_PAD_Y;
        private boolean grabbedScreen = false;
        private float grabStartY = 0.0F;
        private float currentDiffY = 0.0F;
        private float scrollLowerBound;
        private float scrollUpperBound;
        public boolean justSorted;
        public AbstractCard hoveredCard;
        private ScrollBar scrollBar;

        public PreviewScreen() {
            scrollBar = new ScrollBar(PreviewScreen.this);
            scrollLowerBound = -Settings.DEFAULT_SCROLL_LIMIT;// 47
            scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;// 48

            drawStartX = (float)Settings.WIDTH;// 71
            drawStartX -= (float)(CARDS_PER_LINE - 0) * AbstractCard.IMG_WIDTH * 0.75F;// 72
            drawStartX -= (float)(CARDS_PER_LINE - 1) * Settings.CARD_VIEW_PAD_X;// 73
            drawStartX /= 2.0F;// 74
            drawStartX += AbstractCard.IMG_WIDTH * 0.75F / 2.0F;// 75
        }

        public void update() {
            boolean isScrollBarScrolling = scrollBar.update();
            if (!isScrollBarScrolling) {
                updateScrolling();
            }
            updateCards();
        }

        public void render(SpriteBatch sb) {
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT - 64.0F * Settings.scale);
            scrollBar.render(sb);
            renderCards(sb);
        }

        private void updateCards() {
            this.hoveredCard = null;// 337
            int lineNum = 0;// 338
            ArrayList<AbstractCard> cards = cardsToRender.group;// 340

            for(int i = 0; i < cards.size(); ++i) {// 342
                int mod = i % CARDS_PER_LINE;// 343
                if (mod == 0 && i != 0) {// 344
                    ++lineNum;// 345
                }

                cards.get(i).target_x = drawStartX + (float)mod * padX;// 347
                cards.get(i).target_y = drawStartY + this.currentDiffY - (float)lineNum * padY;// 348
                cards.get(i).update();// 349
                cards.get(i).updateHoverLogic();// 350
                if (cards.get(i).hb.hovered) {// 352
                    this.hoveredCard = cards.get(i);// 353
                }
            }

            if (justSorted) {
                for (AbstractCard c : cards) {
                    c.current_x = c.target_x;
                    c.update();
                }
                justSorted = false;
            }

        }

        public void renderCards(SpriteBatch sb) {
            cardsToRender.renderInLibrary(sb);// 503
            cardsToRender.renderTip(sb);
            if (this.hoveredCard != null) {// 426
                this.hoveredCard.renderHoverShadow(sb);// 427
                this.hoveredCard.renderInLibrary(sb);// 428
            }
        }

        private void updateScrolling() {
            int y = InputHelper.mY;// 366
            if (!this.grabbedScreen) {// 368
                if (InputHelper.scrolledDown) {// 369
                    this.currentDiffY += Settings.SCROLL_SPEED;// 370
                } else if (InputHelper.scrolledUp) {// 371
                    this.currentDiffY -= Settings.SCROLL_SPEED;// 372
                }

                if (InputHelper.justClickedLeft) {// 375
                    //this.grabbedScreen = true;// 376
                    this.grabStartY = (float)y - this.currentDiffY;// 377
                }
            } else if (InputHelper.isMouseDown) {// 380
                this.currentDiffY = (float)y - this.grabStartY;// 381
            } else {
                this.grabbedScreen = false;// 383
            }

            this.resetScrolling();// 387
            this.updateBarPosition();// 388
        }// 389

        public void calculateScrollBounds() {
            int size = cardsToRender.size();// 395
            int scrollTmp = 0;// 397
            if (size > CARDS_PER_LINE * 2) {// 398
                scrollTmp = size / CARDS_PER_LINE - 2;// 399
                if (size % CARDS_PER_LINE != 0) {// 400
                    ++scrollTmp;// 401
                }

                this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT + (float)scrollTmp * padY;// 403
            } else {
                this.scrollUpperBound = Settings.DEFAULT_SCROLL_LIMIT;// 405
            }

        }// 407

        private void resetScrolling() {
            if (this.currentDiffY < this.scrollLowerBound) {// 413
                this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollLowerBound);// 414
            } else if (this.currentDiffY > this.scrollUpperBound) {// 415
                this.currentDiffY = MathHelper.scrollSnapLerpSpeed(this.currentDiffY, this.scrollUpperBound);// 416
            }

        }// 418

        public void scrolledUsingBar(float newPercent) {
            this.currentDiffY = MathHelper.valueFromPercentBetween(this.scrollLowerBound, this.scrollUpperBound, newPercent);// 546
            this.updateBarPosition();// 547
        }// 548

        private void updateBarPosition() {
            float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound, this.scrollUpperBound, this.currentDiffY);// 551
            this.scrollBar.parentScrolledToPercent(percent);// 552
        }// 553
    }
}
