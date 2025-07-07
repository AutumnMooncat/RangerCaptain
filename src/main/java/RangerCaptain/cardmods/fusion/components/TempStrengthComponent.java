package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.ApplyPowerActionWithFollowup;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTempApplyComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TempStrengthComponent extends AbstractTempApplyComponent {
    public static final String ID = MainModfile.makeID(StrengthComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public TempStrengthComponent(int base) {
        this(base, ComponentTarget.SELF, true);
    }

    public TempStrengthComponent(int base, ComponentTarget target, boolean increase) {
        super(ID, base, target, increase);
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
        if (increase) {
            Wiz.sequenceActions(toTop,
                    new ApplyPowerAction(target, Wiz.adp(), new StrengthPower(target, amount)),
                    new ApplyPowerAction(target, Wiz.adp(), new LoseStrengthPower(target, amount))
            );
        } else {
            Wiz.sequenceActions(toTop,
                    new ApplyPowerActionWithFollowup(
                            new ApplyPowerAction(target, Wiz.adp(), new StrengthPower(target, -amount)),
                            new ApplyPowerAction(target, Wiz.adp(), new GainStrengthPower(target, amount))
                    )
            );
        }
    }


    @Override
    public AbstractComponent makeCopy() {
        return new TempStrengthComponent(baseAmount, target, increase);
    }
}
