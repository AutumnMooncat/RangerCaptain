package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.OnModifyMaxHPPower;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class OnModifyMaxHPPatch {
    @SpirePatch2(clz = AbstractCreature.class, method = "increaseMaxHp")
    @SpirePatch2(clz = AbstractCreature.class, method = "decreaseMaxHealth")
    public static class MaxHPModified {
        @SpirePrefixPatch
        public static void modifyMaxHP(AbstractCreature __instance, @ByRef int[] amount) {
            for (AbstractPower pow : __instance.powers) {
                if (pow instanceof OnModifyMaxHPPower) {
                    amount[0] = ((OnModifyMaxHPPower) pow).onModifyMaxHP(__instance, amount[0]);
                }
            }
        }
    }
}
