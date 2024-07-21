package RangerCaptain.damageMods;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SadisticDamage extends AbstractDamageModifier {
    private final int amount;

    public SadisticDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (target != null && target.powers.stream().anyMatch(p -> p.type == AbstractPower.PowerType.DEBUFF)) {
            damage += amount;
        }
        return damage;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new SadisticDamage(amount);
    }
}
