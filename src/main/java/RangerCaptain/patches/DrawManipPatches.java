package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.DrawManipPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DrawManipPatches {
    @SpirePatch2(clz = DrawCardAction.class, method = "update")
    public static class ModifyAmount {
        @SpirePrefixPatch
        public static void modify(DrawCardAction __instance, boolean ___clearDrawHistory) {
            if (___clearDrawHistory) {
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof DrawManipPower) {
                        __instance.amount = ((DrawManipPower) p).changeAmount(__instance.amount);
                    }
                }
            }
        }
    }
}
