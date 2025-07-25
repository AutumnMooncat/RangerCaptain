package RangerCaptain.damageMods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SadisticForEachDamage extends AbstractDamageModifier {
    private final int amount;

    public SadisticForEachDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (target != null) {
            for (AbstractPower power : target.powers) {
                if (power.type == AbstractPower.PowerType.DEBUFF) {
                    damage += amount;
                }
            }
        }
        return damage;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new SadisticForEachDamage(amount);
    }
}
