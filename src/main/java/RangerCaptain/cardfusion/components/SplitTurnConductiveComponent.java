package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractSplitTurnApplyComponent;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.powers.NextTurnPowerPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SplitTurnConductiveComponent extends AbstractSplitTurnApplyComponent {
    public static final String ID = MainModfile.makeID(SplitTurnConductiveComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public SplitTurnConductiveComponent(float base) {
        this(base, ComponentTarget.ENEMY);
    }

    public SplitTurnConductiveComponent(float base, ComponentTarget target) {
        super(ID, base, target);
    }

    @Override
    public String getName() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String getKeyword() {
        return CARD_TEXT[0];
    }

    @Override
    public void doApply(AbstractCreature target, int amount, boolean toTop) {
        Wiz.sequenceActions(toTop,
                new ApplyPowerAction(target, Wiz.adp(), new ConductivePower(target, Wiz.adp(), amount)),
                new ApplyPowerAction(target, Wiz.adp(), new NextTurnPowerPower(target, new ConductivePower(target, Wiz.adp(), amount)))
        );
    }

    @Override
    public AbstractComponent makeCopy() {
        return new SplitTurnConductiveComponent(baseAmount, target);
    }
}
