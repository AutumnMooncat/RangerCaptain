package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.powers.MultitargetPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MultitargetComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(MultitargetComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public MultitargetComponent(int base) {
        super(ID, base, ComponentTarget.SELF);
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
    public AbstractPower getPower(AbstractCreature target, int amount) {
        return new MultitargetPower(target, amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new MultitargetComponent(baseAmount);
    }
}
