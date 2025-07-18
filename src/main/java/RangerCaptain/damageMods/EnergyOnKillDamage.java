package RangerCaptain.damageMods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class EnergyOnKillDamage extends AbstractDamageModifier {
    public int amount;

    public EnergyOnKillDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        if (lastDamageTaken > 0 && target.currentHealth - lastDamageTaken <= 0) {
            addToTop(new GainEnergyAction(amount));
        }
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new EnergyOnKillDamage(amount);
    }
}
