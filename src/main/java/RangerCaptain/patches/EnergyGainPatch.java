package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class EnergyGainPatch {
    @SpirePatch(clz = EnergyPanel.class, method = "addEnergy")
    @SpirePatch(clz = EnergyPanel.class, method = "setEnergy")
    public static class TriggerOnEnergyGain {
        @SpirePrefixPatch
        public static void energyGain(@ByRef int[] energyAmount) {
            for (AbstractPower pow : Wiz.adp().powers) {
                if (pow instanceof OnGainEnergyPower) {
                    energyAmount[0] = ((OnGainEnergyPower) pow).onGainEnergy(energyAmount[0]);
                }
            }
        }
    }

    public interface OnGainEnergyPower {
        int onGainEnergy(int amount);
    }
}
