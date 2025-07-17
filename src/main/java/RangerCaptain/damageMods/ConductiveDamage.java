package RangerCaptain.damageMods;

import RangerCaptain.powers.ConductivePower;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ConductiveDamage extends AbstractDamageModifier {

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastDamageTaken, int overkillAmount, AbstractCreature target) {
        if (lastDamageTaken > 0) {
            addToTop(new ApplyPowerAction(target, info.owner, new ConductivePower(target, info.owner, lastDamageTaken)));
        }
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ConductiveDamage();
    }
}
