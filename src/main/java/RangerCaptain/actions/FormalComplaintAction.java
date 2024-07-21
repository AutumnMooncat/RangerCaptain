package RangerCaptain.actions;

import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class FormalComplaintAction extends AbstractGameAction {

    public FormalComplaintAction(AbstractCreature target) {
        this.target = target;
        this.source = Wiz.adp();
    }

    @Override
    public void update() {
        AbstractPower vuln = source.getPower(VulnerablePower.POWER_ID);
        if (vuln != null) {
            addToTop(new ThrowPowerAction(target, new VulnerablePower(target, vuln.amount, false)));
            addToTop(new RemoveSpecificPowerAction(source, source, vuln));
        }

        AbstractPower weak = source.getPower(WeakPower.POWER_ID);
        if (weak != null) {
            addToTop(new ThrowPowerAction(target, new WeakPower(target, weak.amount, false)));
            addToTop(new RemoveSpecificPowerAction(source, source, weak));
        }
        this.isDone = true;
    }
}
