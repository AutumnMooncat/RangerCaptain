package RangerCaptain.damageMods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BootDamage extends AbstractDamageModifier {
    public int amount;

    public BootDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && damageAmount < amount) {
            damageAmount = amount;
        }
        return damageAmount;
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new BootDamage(amount);
    }
}