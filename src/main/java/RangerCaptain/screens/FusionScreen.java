package RangerCaptain.screens;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.FusionScreenPatches;
import RangerCaptain.util.Wiz;
import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.buttons.CardSelectConfirmButton;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

public class FusionScreen extends CustomScreen {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(FusionScreen.class.getSimpleName()));
    public static final String[] TEXT = uiStrings.TEXT;
    public AbstractCard baseCard;
    public AbstractCard donorCard;
    public AbstractCard hoveredCard;
    private AbstractCard fusionPreviewCard;
    public boolean wereCardsRetrieved;
    private String message;
    public CardSelectConfirmButton button;
    private final PeekButton peekButton;
    public static final float MIN_HOVER_DIST = 64f * Settings.scale;
    private boolean waitThenClose;
    private float waitToCloseTimer;
    private CardGroup hand;
    public static final float HOVER_CARD_Y_POSITION = 210.0F * Settings.scale;
    private static final float BANNER_HEIGHT = Settings.HEIGHT - 100.0F * Settings.scale;
    private static final float BUMP_X = 64f * Settings.scale;
    private static final float CARD_HEIGHT = Settings.HEIGHT / 2.0F + 200.0F * Settings.scale;
    private static final float FUSION_CARD_SCALE = 1f;
    private static final int ARROW_W = 64;
    private float arrowScale1;
    private float arrowScale2;
    private float arrowScale3;
    private float arrowTimer;
    private boolean justPreview;
    public boolean didFuse;
    private boolean justPeeked;


    public static class Enum {
        @SpireEnum
        public static AbstractDungeon.CurrentScreen FUSION_SCREEN;
    }

    public FusionScreen() {
        this.baseCard = null;
        this.donorCard = null;
        this.hoveredCard = null;
        this.fusionPreviewCard = null;
        this.wereCardsRetrieved = false;
        this.message = "";
        this.button = new CardSelectConfirmButton();
        this.peekButton = new PeekButton();
        this.waitThenClose = false;
        this.waitToCloseTimer = 0.0F;
        this.arrowScale1 = 0.75F;
        this.arrowScale2 = 0.75F;
        this.arrowScale3 = 0.75F;
        this.arrowTimer = 0.0F;
    }

    public void open(boolean justPreview) {
        this.justPreview = justPreview;
        prep();
        button.isDisabled = false;
        button.disable();
        fusionPreviewCard = null;
        button.hideInstantly();// 596
        button.show();// 597
        peekButton.hideInstantly();// 598
        peekButton.show();// 599
        updateMessage();// 601
    }

    @Override
    public void reopen() {
        AbstractDungeon.overlayMenu.showBlackScreen(0.75F);
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);
    }

    @Override
    public void close() {
        AbstractDungeon.overlayMenu.cancelButton.hideInstantly();
        genericScreenOverlayReset();
        AbstractDungeon.overlayMenu.showCombatPanels();
        Settings.hideRelics = false;
        FusionScreenPatches.didHideRelics = false;
    }

    @Override
    public void update() {
        updateControllerInput();
        peekButton.update();
        if (!PeekButton.isPeeking) {
            if (justPeeked) {
                justPeeked = false;
                AbstractDungeon.overlayMenu.cancelButton.showInstantly(TEXT[3]);
            }
            updateHand();
            updateSelectedCards();
            if (waitThenClose) {
                waitToCloseTimer -= Gdx.graphics.getDeltaTime();
                if (waitToCloseTimer < 0.0F) {
                    waitThenClose = false;
                    AbstractDungeon.closeCurrentScreen();
                }
            }

            if (!justPreview) {
                button.update();
                if (button.hb.clicked || CInputActionSet.proceed.isJustPressed() || InputActionSet.confirm.isJustPressed()) {
                    CInputActionSet.proceed.unpress();
                    button.hb.clicked = false;
                    if (baseCard != null && donorCard != null) {
                        InputHelper.justClickedLeft = false;
                        didFuse = true;
                        AbstractDungeon.closeCurrentScreen();
                    }
                }
            }
        } else if (!justPeeked) {
            justPeeked = true;
            AbstractDungeon.overlayMenu.cancelButton.hideInstantly();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        AbstractDungeon.player.hand.render(sb);
        AbstractDungeon.overlayMenu.energyPanel.render(sb);
        if (!PeekButton.isPeeking) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, message, (float)(Settings.WIDTH / 2), BANNER_HEIGHT, Settings.CREAM_COLOR);
            if (!justPreview) {
                button.render(sb);
            }
            if (baseCard != null) {
                baseCard.render(sb);
            }
            if (donorCard != null) {
                donorCard.render(sb);
            }
            if (fusionPreviewCard != null) {
                renderArrows(sb);
                fusionPreviewCard.current_x = Settings.WIDTH * 0.5F;
                fusionPreviewCard.current_y = CARD_HEIGHT;
                fusionPreviewCard.target_x = Settings.WIDTH * 0.5F;
                fusionPreviewCard.target_y = CARD_HEIGHT;
                fusionPreviewCard.render(sb);
                fusionPreviewCard.updateHoverLogic();
                fusionPreviewCard.renderCardTip(sb);
            }
        }

        peekButton.render(sb);
        AbstractDungeon.overlayMenu.combatDeckPanel.render(sb);
        AbstractDungeon.overlayMenu.discardPilePanel.render(sb);
        AbstractDungeon.overlayMenu.exhaustPanel.render(sb);
    }

    private void renderArrows(SpriteBatch sb) {
        sb.setColor(Color.WHITE);

        float x = Settings.WIDTH / 2.0F - 320.0F * Settings.scale - BUMP_X;
        float y = Settings.HEIGHT / 2.0F + 160.0F * Settings.scale;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        x += ARROW_W * Settings.scale;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        x += ARROW_W * Settings.scale;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        x = (float)Settings.WIDTH / 2.0F + 128.0F * Settings.scale + BUMP_X;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale3 * Settings.scale, arrowScale3 * Settings.scale, 0.0F, 0, 0, 64, 64, true, false);
        x += ARROW_W * Settings.scale;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale2 * Settings.scale, arrowScale2 * Settings.scale, 0.0F, 0, 0, 64, 64, true, false);
        x += ARROW_W * Settings.scale;
        sb.draw(ImageMaster.UPGRADE_ARROW, x, y, ARROW_W/2f, ARROW_W/2f, ARROW_W, ARROW_W, arrowScale1 * Settings.scale, arrowScale1 * Settings.scale, 0.0F, 0, 0, 64, 64, true, false);

        arrowTimer += Gdx.graphics.getDeltaTime() * 2.0F;
        arrowScale1 = 0.8F + (MathUtils.cos(arrowTimer) + 1.0F) / 8.0F;
        arrowScale2 = 0.8F + (MathUtils.cos(arrowTimer - 0.8F) + 1.0F) / 8.0F;
        arrowScale3 = 0.8F + (MathUtils.cos(arrowTimer - 1.6F) + 1.0F) / 8.0F;
    }// 781

    @Override
    public void openingSettings() {
        // Required if you want to reopen your screen when the settings screen closes
        AbstractDungeon.previousScreen = curScreen();
    }

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return Enum.FUSION_SCREEN;
    }

    private void updateControllerInput() {
        if (Settings.isControllerMode) {
            boolean inHand = true;
            boolean anyHovered = false;
            int index = 0;
            int y;

            if (baseCard != null) {
                if (baseCard.hb.hovered) {
                    anyHovered = true;
                    inHand = false;
                }
            }

            if (donorCard != null) {
                if (donorCard.hb.hovered) {
                    anyHovered = true;
                    inHand = false;
                }
            }

            if (inHand) {
                for (AbstractCard card : hand.group) {
                    if (card == hoveredCard) {
                        anyHovered = true;
                        break;
                    }
                    index++;
                }
            }

            if (!anyHovered) {
                if (!hand.group.isEmpty()) {
                    setHoveredCard(hand.group.get(0));
                    Gdx.input.setCursorPosition((int) hoveredCard.hb.cX, Settings.HEIGHT - (int) hoveredCard.hb.cY);
                } else if (baseCard != null) {
                    Gdx.input.setCursorPosition((int) baseCard.hb.cX, Settings.HEIGHT - (int) baseCard.hb.cY);
                } else if (donorCard != null) {
                    Gdx.input.setCursorPosition((int) donorCard.hb.cX, Settings.HEIGHT - (int) donorCard.hb.cY);
                }
            } else if (!inHand) {
                if ((CInputActionSet.down.isJustPressed() || CInputActionSet.altDown.isJustPressed()) && !hand.group.isEmpty()) {
                    if (hand.group.get(index).hb.cY < 0.0F) {
                        y = 1;
                    } else {
                        y = (int)(hand.group.get(index)).hb.cY;
                    }
                    Gdx.input.setCursorPosition((int)(hand.group.get(index)).hb.cX, Settings.HEIGHT - y);
                    setHoveredCard(hand.group.get(index));
                } else if (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed()) {
                    if (baseCard != null && donorCard != null) {
                        if (baseCard.hb.hovered) {
                            Gdx.input.setCursorPosition((int) donorCard.hb.cX, Settings.HEIGHT - (int) donorCard.hb.cY);
                        } else {
                            Gdx.input.setCursorPosition((int) baseCard.hb.cX, Settings.HEIGHT - (int) baseCard.hb.cY);
                        }
                        unhoverCard(hoveredCard);
                    }
                } else if (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed()) {
                    if (baseCard != null && donorCard != null) {
                        if (baseCard.hb.hovered) {
                            Gdx.input.setCursorPosition((int) donorCard.hb.cX, Settings.HEIGHT - (int) donorCard.hb.cY);
                        } else {
                            Gdx.input.setCursorPosition((int) baseCard.hb.cX, Settings.HEIGHT - (int) baseCard.hb.cY);
                        }
                        unhoverCard(hoveredCard);
                    }
                } else if (CInputActionSet.select.isJustPressed()) {
                    CInputActionSet.select.unpress();
                    if (baseCard != null && baseCard.hb.hovered) {
                        hand.addToTop(baseCard);
                        baseCard = null;
                        refreshSelectedCards();
                        updateMessage();
                        hand.refreshHandLayout();
                    } else if (donorCard != null && donorCard.hb.hovered) {
                        hand.addToTop(donorCard);
                        donorCard = null;
                        refreshSelectedCards();
                        updateMessage();
                        hand.refreshHandLayout();
                    }
                }
            } else if ((CInputActionSet.up.isJustPressed() || CInputActionSet.altUp.isJustPressed()) && (baseCard != null || donorCard != null)) {
                if (donorCard != null) {
                    if (donorCard.hb.cY < 0.0F) {
                        y = 1;
                    } else {
                        y = (int) donorCard.hb.cY;
                    }
                    unhoverCard(hoveredCard);
                    Gdx.input.setCursorPosition((int) donorCard.hb.cX, Settings.HEIGHT - y);
                } else {
                    if (baseCard.hb.cY < 0.0F) {
                        y = 1;
                    } else {
                        y = (int) baseCard.hb.cY;
                    }
                    unhoverCard(hoveredCard);
                    Gdx.input.setCursorPosition((int) baseCard.hb.cX, Settings.HEIGHT - y);
                }
            } else if (hand.size() > 1 && (CInputActionSet.left.isJustPressed() || CInputActionSet.altLeft.isJustPressed())) {
                index--;
                if (index < 0)
                    index = hand.size() - 1;
                unhoverCard(hoveredCard);
                setHoveredCard(hand.group.get(index));
                Gdx.input.setCursorPosition((int)hoveredCard.hb.cX, Settings.HEIGHT - (int)hoveredCard.hb.cY);
            } else if (hand.size() > 1 && (CInputActionSet.right.isJustPressed() || CInputActionSet.altRight.isJustPressed())) {
                index++;
                if (index > hand.size() - 1)
                    index = 0;
                unhoverCard(hoveredCard);
                setHoveredCard(hand.group.get(index));
                Gdx.input.setCursorPosition((int)hoveredCard.hb.cX, Settings.HEIGHT - (int)hoveredCard.hb.cY);
            } else if (hand.size() == 1 && hoveredCard == null) {
                setHoveredCard(hand.group.get(index));
            }
        }
    }

    private void unhoverCard(AbstractCard card) {
        if (card != null) {
            card.targetDrawScale = 0.7F;
            card.hoverTimer = 0.25F;
            card.unhover();
            hand.refreshHandLayout();
        }
    }

    private void setHoveredCard(AbstractCard card) {
        hoveredCard = card;
        hoveredCard.current_y = HOVER_CARD_Y_POSITION;
        hoveredCard.target_y = HOVER_CARD_Y_POSITION;
        hoveredCard.drawScale = 1.0F;
        hoveredCard.targetDrawScale = 1.0F;
        hoveredCard.setAngle(0.0F, true);
        hand.hoverCardPush(hoveredCard);
    }

    private void updateHand() {
        if (!Settings.isControllerMode) {
            hoverCheck();
            unhoverCheck();
        }

        startDraggingCardCheck();
        hotkeyCheck();
    }

    private void refreshSelectedCards() {
        if (baseCard != null) {
            baseCard.target_y = CARD_HEIGHT;
            baseCard.target_x = Settings.WIDTH * 0.25F - BUMP_X;
        }

        if (donorCard != null) {
            donorCard.target_y = CARD_HEIGHT;
            donorCard.target_x = Settings.WIDTH * 0.75F + BUMP_X;
        }

        if (baseCard != null && donorCard != null) {
            button.enable();
        } else {
            button.disable();
        }
    }

    private void hoverCheck() {
        if (hoveredCard == null) {
            hoveredCard = hand.getHoveredCard();
            if (hoveredCard != null) {
                hoveredCard.current_y = HOVER_CARD_Y_POSITION;
                hoveredCard.target_y = HOVER_CARD_Y_POSITION;
                hoveredCard.drawScale = 1.0F;
                hoveredCard.targetDrawScale = 1.0F;
                hoveredCard.setAngle(0.0F, true);
                hand.hoverCardPush(hoveredCard);
            }
        }
    }

    private void unhoverCheck() {
        if (hoveredCard != null && !hoveredCard.isHoveredInHand(1.0F)) {
            hoveredCard.targetDrawScale = 0.7F;
            hoveredCard.hoverTimer = 0.25F;
            hoveredCard.unhover();
            hoveredCard = null;
            hand.refreshHandLayout();
        }
    }

    private void startDraggingCardCheck() {
        if (hoveredCard != null && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())) {
            CInputActionSet.select.unpress();
            selectHoveredCard();
        }
    }

    private void selectHoveredCard() {
        if (baseCard == null) {
            InputHelper.justClickedLeft = false;
            CardCrawlGame.sound.play("CARD_OBTAIN");
            hand.removeCard(hoveredCard);
            hand.refreshHandLayout();
            hoveredCard.setAngle(0f, false);
            baseCard = hoveredCard;
            refreshSelectedCards();
            hoveredCard = null;
            updateMessage();
        } else if (donorCard == null) {
            InputHelper.justClickedLeft = false;
            CardCrawlGame.sound.play("CARD_OBTAIN");
            hand.removeCard(hoveredCard);
            hand.refreshHandLayout();
            hoveredCard.setAngle(0f, false);
            donorCard = hoveredCard;
            refreshSelectedCards();
            hoveredCard = null;
            updateMessage();
        } else {
            InputHelper.justClickedLeft = false;
            CardCrawlGame.sound.play("CARD_OBTAIN");
            hand.removeCard(hoveredCard);
            hoveredCard.setAngle(0f, false);
            hand.addToTop(donorCard);
            donorCard = hoveredCard;
            hoveredCard = null;
            refreshSelectedCards();
            hand.refreshHandLayout();
            updateMessage();
        }

        InputHelper.moveCursorToNeutralPosition();
    }

    private void hotkeyCheck() {
        AbstractCard hotkeyCard = InputHelper.getCardSelectedByHotkey(hand);
        if (hotkeyCard != null) {
            hoveredCard = hotkeyCard;
            hoveredCard.setAngle(0.0F, false);
            selectHoveredCard();
        }
    }

    private void updateSelectedCards() {
        if (baseCard != null) {
            baseCard.update();
            baseCard.current_x = MathHelper.cardLerpSnap(baseCard.current_x, baseCard.target_x);
            baseCard.current_y = MathHelper.cardLerpSnap(baseCard.current_y, baseCard.target_y);
            baseCard.hb.update();
            baseCard.targetDrawScale = 0.75F;
            if (Math.abs(baseCard.current_x - baseCard.target_x) < MIN_HOVER_DIST && baseCard.hb.hovered) {
                baseCard.targetDrawScale = 0.85F;
            }

            if (!waitThenClose && Math.abs(baseCard.current_x - baseCard.target_x) < MIN_HOVER_DIST && baseCard.hb.hovered && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())) {
                InputHelper.justClickedLeft = false;
                AbstractDungeon.player.hand.addToTop(baseCard);
                baseCard = null;
                refreshSelectedCards();
                updateMessage();
                if (Settings.isControllerMode) {
                    hand.refreshHandLayout();
                }
            }
        }

        if (donorCard != null) {
            donorCard.update();
            donorCard.current_x = MathHelper.cardLerpSnap(donorCard.current_x, donorCard.target_x);
            donorCard.current_y = MathHelper.cardLerpSnap(donorCard.current_y, donorCard.target_y);
            donorCard.hb.update();
            donorCard.targetDrawScale = 0.75F;
            if (Math.abs(donorCard.current_x - donorCard.target_x) < MIN_HOVER_DIST && donorCard.hb.hovered) {
                donorCard.targetDrawScale = 0.85F;
            }

            if (!waitThenClose && Math.abs(donorCard.current_x - donorCard.target_x) < MIN_HOVER_DIST && donorCard.hb.hovered && (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed())) {
                InputHelper.justClickedLeft = false;
                AbstractDungeon.player.hand.addToTop(donorCard);
                donorCard = null;
                refreshSelectedCards();
                updateMessage();
                if (Settings.isControllerMode) {
                    hand.refreshHandLayout();
                }
            }
        }

        if (baseCard == null || donorCard == null) {
            button.disable();
        }

    }

    private void updateMessage() {
        if (baseCard == null) {
            message = TEXT[0];
            fusionPreviewCard = null;
        } else if (donorCard == null) {
            message = TEXT[1];
            fusionPreviewCard = null;
        } else {
            message = TEXT[2];
            fusionPreviewCard = Wiz.fuse(baseCard, donorCard);
            fusionPreviewCard.applyPowers();
            fusionPreviewCard.drawScale = FUSION_CARD_SCALE;
            fusionPreviewCard.targetDrawScale = FUSION_CARD_SCALE;
        }
    }

    private void prep() {
        AbstractDungeon.player.resetControllerValues();
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.unhover();
        }
        
        AbstractDungeon.topPanel.unhoverHitboxes();
        AbstractDungeon.actionManager.cleanCardQueue();
        hand = AbstractDungeon.player.hand;
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.getMonsters().hoveredMonster = null;
        waitThenClose = false;
        waitToCloseTimer = 0.0F;
        baseCard = null;
        donorCard = null;
        hoveredCard = null;
        didFuse = false;
        justPeeked = false;
        wereCardsRetrieved = false;
        Settings.hideRelics = true;
        FusionScreenPatches.didHideRelics = true;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = Enum.FUSION_SCREEN;
        AbstractDungeon.player.hand.stopGlowing();
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.overlayMenu.showBlackScreen(0.75F);
        if (Settings.isControllerMode) {
            Gdx.input.setCursorPosition((int)hand.group.get(0).hb.cX, (int)hand.group.get(0).hb.cY);
        }
        AbstractDungeon.overlayMenu.cancelButton.show(TEXT[3]);
    }
}
