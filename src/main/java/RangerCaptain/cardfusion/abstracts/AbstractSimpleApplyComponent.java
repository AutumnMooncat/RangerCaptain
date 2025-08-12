package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractSimpleApplyComponent extends AbstractApplyComponent {

    public AbstractSimpleApplyComponent(String ID, float base, ComponentTarget target) {
        super(ID, base, target);
        isSimple = true;
    }

    @Override
    public void updatePrio() {
        priority = SIMPLE_APPLY_PRIO + target.ordinal() * 2;
    }

    public abstract AbstractPower getPower(AbstractCreature target, int amount);

    @Override
    public void doApply(AbstractCreature target, int amount, boolean toTop) {
        if (toTop) {
            addToTop(new ApplyPowerAction(target, Wiz.adp(), getPower(target, amount)));
        } else {
            addToBot(new ApplyPowerAction(target, Wiz.adp(), getPower(target, amount)));
        }
    }
}
