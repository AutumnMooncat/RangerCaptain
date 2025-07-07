package RangerCaptain.actions;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IncreaseDebuffsAction extends AbstractGameAction {

    public IncreaseDebuffsAction(AbstractCreature target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void update() {
        if (target != null) {
            for (AbstractPower pow : target.powers) {
                if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof NonStackablePower)) {
                    if (pow.amount > 0) {
                        pow.stackPower(amount);
                    } else if (pow.canGoNegative) {
                        pow.stackPower(-amount);
                    }
                    pow.updateDescription();
                }
            }
        }
        isDone = true;
    }
}
