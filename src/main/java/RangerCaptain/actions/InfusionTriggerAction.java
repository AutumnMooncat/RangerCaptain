package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.AbstractInfusion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class InfusionTriggerAction extends AbstractGameAction {
    private AbstractInfusion infusion;
    int directAmount;
    int relicAmount;

    public InfusionTriggerAction(AbstractInfusion infusion, int directAmount, int relicAmount) {
        this.infusion = infusion;
        this.directAmount = directAmount;
        this.relicAmount = relicAmount;
    }

    @Override
    public void update() {
        MainModfile.infusionTrigger(infusion, directAmount, relicAmount);
        this.isDone = true;
    }
}
