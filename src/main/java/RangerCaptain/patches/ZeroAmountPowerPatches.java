package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class ZeroAmountPowerPatches {
    @SpirePatch2(clz = ApplyPowerAction.class, method = "update")
    public static class FixText {
        static int match = 0;
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(PowerBuffEffect.class.getName())) {
                        match++;
                        if (match == 2) {
                            e.replace("$3 = "+ZeroAmountPowerPatches.class.getName()+".changeText(powerToApply, $3); $_ = $proceed($$);");
                        }
                    }
                }
            };
        }
    }

    public static String changeText(AbstractPower p, String orig) {
        if (p instanceof ZeroAmountPower && p.amount == 0) {
            return p.name;
        }
        return orig;
    }

    public interface ZeroAmountPower {}
}
