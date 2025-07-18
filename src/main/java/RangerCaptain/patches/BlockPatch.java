package RangerCaptain.patches;

import RangerCaptain.powers.GambitPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlockPatch {
    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { AbstractCreature.class, int.class })
    @SpirePatch2(clz = GainBlockAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = { AbstractCreature.class, AbstractCreature.class, int.class })
    public static class DoubleMonsterBlock {
        @SpirePostfixPatch
        public static void plz (GainBlockAction __instance) {
            if (__instance.target instanceof AbstractMonster && __instance.target.hasPower(GambitPower.POWER_ID)) {
                __instance.amount *= 2;
            }
        }
    }
}
