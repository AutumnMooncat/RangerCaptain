package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTempApplyComponent;
import RangerCaptain.powers.LosePowerLaterPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.ThornsPower;

public class TempThornsComponent extends AbstractTempApplyComponent {
    public static final String ID = MainModfile.makeID(ThornsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public TempThornsComponent(int base) {
        super(ID, base, ComponentTarget.SELF, true);
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
                new ApplyPowerAction(target, Wiz.adp(), new ThornsPower(target, amount)),
                new ApplyPowerAction(target, Wiz.adp(), new LosePowerLaterPower(target, new ThornsPower(target, amount), amount))
        );
    }


    @Override
    public AbstractComponent makeCopy() {
        return new TempThornsComponent(baseAmount);
    }
}
