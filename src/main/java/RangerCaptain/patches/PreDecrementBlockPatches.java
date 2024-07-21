package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.PreDecrementBlockPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class PreDecrementBlockPatches {
    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class PreBlock {
        @SpireInsertPatch(locator = Locator.class, localvars = "damageAmount")
        public static void trigger(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractPower p : __instance.powers) {
                if (p instanceof PreDecrementBlockPower) {
                    damageAmount[0] = ((PreDecrementBlockPower) p).preBlockCalc(__instance, info, damageAmount[0]);
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
