package RangerCaptain.damageMods;

import RangerCaptain.powers.ExposedPower;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ExposedDamage extends AbstractDamageModifier {
    int amount;

    public ExposedDamage(int amount) {
        this.amount = amount;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        addToTop(new ApplyPowerAction(target, info.owner, new ExposedPower(target, amount)));
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ExposedDamage(amount);
    }
}
