package RangerCaptain.damageMods;

import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PoisonPowderDamage extends AbstractDamageModifier {
    int amount;

    public PoisonPowderDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target instanceof AbstractMonster && amount > 0) {
            Wiz.applyToEnemy((AbstractMonster) target, new PoisonPower(target, info.owner, amount));
        }
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new PoisonPowderDamage(amount);
    }
}
