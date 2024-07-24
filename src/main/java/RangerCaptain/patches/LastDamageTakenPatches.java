package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.LastDamageTakenUpdatePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class LastDamageTakenPatches {
    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class LastDamageTakenHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(AbstractPlayer __instance, DamageInfo info) {
            for (AbstractPower pow : __instance.powers) {
                if (pow instanceof LastDamageTakenUpdatePower) {
                    ((LastDamageTakenUpdatePower) pow).onLastDamageTakenUpdate(info, __instance.lastDamageTaken);
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(Math.class, "min");
                return new int[]{LineFinder.findInOrder(ctBehavior, m)[0]+1};
            }
        }
    }
}
