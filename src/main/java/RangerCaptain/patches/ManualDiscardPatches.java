package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.OnDiscardPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class ManualDiscardPatches {
    @SpirePatch2(clz = GameActionManager.class, method = "incrementDiscard")
    public static class PowerPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz() {
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnDiscardPower) {
                    ((OnDiscardPower) p).onManualDiscard();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractPlayer.class, "updateCardsOnDiscard");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
