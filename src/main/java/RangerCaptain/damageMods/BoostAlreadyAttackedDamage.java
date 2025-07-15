package RangerCaptain.damageMods;

import RangerCaptain.patches.CardCounterPatches;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BoostAlreadyAttackedDamage extends AbstractDamageModifier {
    private final int amount;

    public BoostAlreadyAttackedDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        if (target != null && CardCounterPatches.AttackCountField.attackedThisTurn.get(target) > 0) {
            damage += amount;
        }
        return damage;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new BoostAlreadyAttackedDamage(amount);
    }
}
