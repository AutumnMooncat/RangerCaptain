package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class OnChangeEnergyPatches {
    public interface OnChangeEnergyObject {
        default int onUseEnergy(int amount) {
            return amount;
        }
        default int onGainEnergy(int amount) {
            return amount;
        }
        default int onSetEnergy(int amount) {
            return amount;
        }
    }

    @SpirePatch2(clz = EnergyPanel.class, method = "useEnergy")
    public static class UseEnergyPatch {
        @SpirePrefixPatch
        public static void plz(@ByRef int[] e) {
            int amount = Math.min(e[0], EnergyPanel.totalCount);
            for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, true)) {
                if (c instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) c).onUseEnergy(amount);
                }
            }
            for (AbstractRelic r : Wiz.adp().relics) {
                if (r instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) r).onUseEnergy(amount);
                }
            }
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) p).onUseEnergy(amount);
                }
            }
            e[0] = Math.max(0, amount);
        }
    }

    @SpirePatch2(clz = EnergyPanel.class, method = "addEnergy")
    public static class GainEnergyPatch {
        @SpirePrefixPatch
        public static void energyGain(@ByRef int[] e) {
            int amount = e[0];
            for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, true)) {
                if (c instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) c).onGainEnergy(amount);
                }
            }
            for (AbstractRelic r : Wiz.adp().relics) {
                if (r instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) r).onGainEnergy(amount);
                }
            }
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) p).onGainEnergy(amount);
                }
            }
            e[0] = Math.max(-EnergyPanel.totalCount, amount);
        }
    }

    @SpirePatch2(clz = EnergyPanel.class, method = "setEnergy")
    public static class SetEnergyPatch {
        @SpirePrefixPatch
        public static void energyGain(@ByRef int[] energy) {
            int amount = energy[0];
            for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, true)) {
                if (c instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) c).onSetEnergy(amount);
                }
            }
            for (AbstractRelic r : Wiz.adp().relics) {
                if (r instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) r).onSetEnergy(amount);
                }
            }
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnChangeEnergyObject) {
                    amount = ((OnChangeEnergyObject) p).onSetEnergy(amount);
                }
            }
            energy[0] = Math.max(0, amount);
        }
    }
}
