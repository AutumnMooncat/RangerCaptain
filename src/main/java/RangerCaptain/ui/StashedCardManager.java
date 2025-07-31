package RangerCaptain.ui;

import RangerCaptain.cards.interfaces.OnOtherCardStashedCard;
import RangerCaptain.cards.interfaces.OnStashedCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.powers.interfaces.OnStashPower;
import RangerCaptain.relics.interfaces.OnStashRelic;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.PerfectedStrike;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import com.megacrit.cardcrawl.vfx.BobEffect;
import javassist.CtBehavior;

public class StashedCardManager {
    public static final float Y_OFFSET = 70f * Settings.scale;
    public static final float X_OFFSET = 100f * Settings.scale;
    public static final CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    private static final BobEffect bob = new BobEffect(3.0f * Settings.scale, 3.0f);
    public static AbstractCard hovered;

    public static void render(SpriteBatch sb) {
        for (AbstractCard card : cards.group) {
            if (card != hovered) {
                card.render(sb);
            }
        }
        if (hovered != null) {
            hovered.render(sb);
            TipHelper.renderTipForCard(hovered, sb, hovered.keywords);
        }
    }

    public static void update() {
        bob.update();
        int i = 0;
        int j = 0;
        hovered = null;
        for (AbstractCard card : cards.group) {
            card.target_y = Wiz.adp().hb.cY + Wiz.adp().hb.height/2f + Y_OFFSET*(j+1) + bob.y;
            card.target_x = Wiz.adp().hb.cX + X_OFFSET * Math.min(9, (cards.size()-1-10*j)) / 2f - X_OFFSET * i;
            card.targetAngle = 0f;
            card.update();
            card.hb.update();
            if (card.hb.hovered && hovered == null) {
                card.targetDrawScale = 0.75f;
                hovered = card;
            } else {
                card.targetDrawScale = 0.2f;
            }
            card.applyPowers();
            i++;
            if (i == 10) {
                i = 0;
                j++;
            }
        }
    }


    public static void addCard(AbstractCard card) {
        addCard(card, true, false);
    }

    public static void addCard(AbstractCard card, boolean playSFX) {
        addCard(card, playSFX, false);
    }

    public static void addCard(AbstractCard card, boolean playSFX, boolean isEndTurn) {
        card.targetAngle = 0f;
        card.beginGlowing();
        cards.addToTop(card);
        if (card instanceof OnStashedCard) {
            ((OnStashedCard) card).onStash();
        }
        for (AbstractCard relevantCard : Wiz.getAllCardsInCardGroups(true, true)) {
            if (relevantCard instanceof OnOtherCardStashedCard) {
                ((OnOtherCardStashedCard) relevantCard).onStashedOther(card);
            }
        }
        for (AbstractRelic r : Wiz.adp().relics) {
            if (r instanceof OnStashRelic) {
                ((OnStashRelic) r).onStash(card, isEndTurn);
            }
        }
        for (AbstractPower p : Wiz.adp().powers) {
            if (p instanceof OnStashPower) {
                ((OnStashPower) p).onStash(card, isEndTurn);
            }
        }
        if (playSFX) {
            CardCrawlGame.sound.play("CARD_REJECT", 0.1F); // CARD_SELECT CARD_REJECT CARD_OBTAIN
        }
        CardCounterPatches.cardsStashedThisTurn++;
        CardCounterPatches.cardsStashedThisCombat++;
    }

    @SpirePatch2(clz = GameActionManager.class, method = "getNextAction")
    public static class MoveCards {
        @SpireInsertPatch(locator = Locator.class)
        public static void move(GameActionManager __instance) {
            while (Wiz.adp().hand.size() < BaseMod.MAX_HAND_SIZE && !cards.isEmpty()) {
                AbstractDungeon.player.hand.addToTop(cards.group.remove(0));
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();
            }
            if (!cards.isEmpty()) {
                Wiz.adp().createHandIsFullDialog();
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnPostDrawRelics");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "render")
    public static class RenderPanel {
        @SpireInsertPatch(locator = Locator.class)
        public static void render(OverlayMenu __instance, SpriteBatch sb) {
            StashedCardManager.render(sb);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ExhaustPanel.class, "render");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "combatUpdate")
    public static class UpdatePile {
        @SpirePostfixPatch
        public static void update(AbstractPlayer __instance) {
            StashedCardManager.update();
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "preBattlePrep")
    @SpirePatch2(clz = AbstractPlayer.class, method = "onVictory")
    public static class EmptyCards {
        @SpirePostfixPatch
        public static void yeet() {
            cards.clear();
        }
    }

    @SpirePatch2(clz = PerfectedStrike.class, method = "countCards")
    public static class CountDiscoveredCardsPlz {
        @SpirePostfixPatch
        public static int getCount(int __result) {
            int projectedStrikes = (int)(StashedCardManager.cards.group.stream().filter(c -> c.hasTag(AbstractCard.CardTags.STRIKE)).count());
            return __result + projectedStrikes;
        }
    }

    /*@SpirePatch2(clz = AbstractCard.class, method = "renderEnergy")
    public static class MakeColoredText {
        private static final Color ENERGY_COST_RESTRICTED_COLOR = new Color(1.0F, 0.3F, 0.3F, 1.0F);
        @SpireInsertPatch(locator = Locator.class, localvars = {"costColor"})
        public static void plz(AbstractCard __instance, @ByRef Color[] costColor) {
            if (AbstractDungeon.player != null && cards.contains(__instance) && !__instance.hasEnoughEnergy()) {
                costColor[0] = ENERGY_COST_RESTRICTED_COLOR;
                costColor[0].a =  __instance.transparency;
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "getCost");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }*/
}
