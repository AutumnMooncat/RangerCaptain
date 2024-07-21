package RangerCaptain.patches;

import RangerCaptain.powers.TapeJamPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class LockIntentPatches {
    @SpirePatch2(clz = AbstractMonster.class, method = "setMove", paramtypez = {String.class, byte.class, AbstractMonster.Intent.class, int.class, int.class, boolean.class})
    public static class ThisIsVeryJank {
        @SpirePrefixPatch
        public static SpireReturn<Void> plz(AbstractMonster __instance) {
            AbstractPower tapejam = __instance.getPower(TapeJamPower.POWER_ID);
            if (tapejam != null) {
                tapejam.flash();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
