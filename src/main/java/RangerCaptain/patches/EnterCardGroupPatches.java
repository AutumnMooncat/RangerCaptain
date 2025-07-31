package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EnterCardGroupPatches {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class LastGroupField {
        public static SpireField<CardGroup> lastPrimaryGroup = new SpireField<>(() -> null);
        public static SpireField<CardGroup> lastActualGroup = new SpireField<>(() -> null);
        public static SpireField<CardGroup> primaryGroup = new SpireField<>(() -> null);
        public static SpireField<CardGroup> actualGroup = new SpireField<>(() -> null);
    }

    @SpirePatch2(clz = CardGroup.class, method = "addToTop")
    @SpirePatch2(clz = CardGroup.class, method = "addToBottom")
    @SpirePatch2(clz = CardGroup.class, method = "addToRandomSpot")
    @SpirePatch2(clz = CardGroup.class, method = "addToHand")
    public static class CardAddedToGroup {
        @SpirePostfixPatch
        public static void checkCard(CardGroup __instance, AbstractCard c) {
            if (Wiz.adp() != null) {
                if (LastGroupField.actualGroup.get(c) != __instance) {
                    LastGroupField.lastActualGroup.set(c, LastGroupField.actualGroup.get(c));
                    LastGroupField.actualGroup.set(c, __instance);
                }
                if (__instance != Wiz.adp().limbo && LastGroupField.primaryGroup.get(c) != __instance) {
                    LastGroupField.lastPrimaryGroup.set(c, LastGroupField.primaryGroup.get(c));
                    LastGroupField.primaryGroup.set(c, __instance);
                    if (__instance == Wiz.adp().hand) {
                        FusionButtonPatches.cardCheck(c);
                    }
                    if (c instanceof OnEnterCardGroupCard) {
                        ((OnEnterCardGroupCard) c).onEnter(__instance);
                    }
                    for (AbstractPower p : Wiz.adp().powers) {
                        if (p instanceof OnEnterCardGroupPower) {
                            ((OnEnterCardGroupPower) p).onEnter(__instance, c);
                        }
                    }
                }
            }
        }
    }

    public interface OnEnterCardGroupCard {
        void onEnter(CardGroup g);
    }

    public interface OnEnterCardGroupPower {
        void onEnter(CardGroup g, AbstractCard c);
    }
}
