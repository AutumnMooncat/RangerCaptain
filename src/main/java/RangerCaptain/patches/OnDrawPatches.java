package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

public class OnDrawPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "draw", paramtypez = int.class)
    public static class CountOnDraw {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void plz(AbstractCard c) {
            CardCounterPatches.cardsDrawnThisCombat.add(c);
            CardCounterPatches.cardsDrawnThisTurn.add(c);
            if (CardCounterPatches.isInitialDraw) {
                CardCounterPatches.initialHand.add(c);
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
