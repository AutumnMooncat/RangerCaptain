package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EnterCardGroupPatches {
    @SpirePatch2(clz = CardGroup.class, method = "addToTop")
    @SpirePatch2(clz = CardGroup.class, method = "addToBottom")
    @SpirePatch2(clz = CardGroup.class, method = "addToRandomSpot")
    @SpirePatch2(clz = CardGroup.class, method = "addToHand")
    public static class CardAddedToGroup {
        @SpirePostfixPatch
        public static void checkCard(CardGroup __instance, AbstractCard c) {
            if (Wiz.adp() != null) {
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

    public interface OnEnterCardGroupCard {
        void onEnter(CardGroup g);
    }

    public interface OnEnterCardGroupPower {
        void onEnter(CardGroup g, AbstractCard c);
    }
}
