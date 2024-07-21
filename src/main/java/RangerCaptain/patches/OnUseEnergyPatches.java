package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class OnUseEnergyPatches {
    public interface OnUseEnergyObject {
        void onUseEnergy(int amount);
    }

    @SpirePatch2(clz = EnergyManager.class, method = "use")
    public static class UseEnergyPatch {
        @SpirePrefixPatch
        public static void trigger(int e) {
            int amount = Math.min(e, EnergyPanel.totalCount);
            for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, true)) {
                if (c instanceof OnUseEnergyObject) {
                    ((OnUseEnergyObject) c).onUseEnergy(amount);
                }
            }
            for (AbstractRelic r : Wiz.adp().relics) {
                if (r instanceof OnUseEnergyObject) {
                    ((OnUseEnergyObject) r).onUseEnergy(amount);
                }
            }
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnUseEnergyObject) {
                    ((OnUseEnergyObject) p).onUseEnergy(amount);
                }
            }
        }
    }
}
