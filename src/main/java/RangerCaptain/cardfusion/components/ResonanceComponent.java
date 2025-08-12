package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ResonanceComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(ResonanceComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ResonanceComponent(float base) {
        this(base, ComponentTarget.ENEMY);
    }

    public ResonanceComponent(float base, ComponentTarget target) {
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
    public boolean scalesWithCost() {
        return false;
    }

    @Override
    public AbstractPower getPower(AbstractCreature target, int amount) {
        return new ResonancePower(target, Wiz.adp(), amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ResonanceComponent(baseAmount, target);
    }
}
