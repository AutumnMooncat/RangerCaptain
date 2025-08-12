package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PoisonComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(PoisonComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public PoisonComponent(float base) {
        this(base, ComponentTarget.ENEMY);
    }

    public PoisonComponent(float base, ComponentTarget target) {
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
    public AbstractPower getPower(AbstractCreature target, int amount) {
        return new PoisonPower(target, Wiz.adp(), amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new PoisonComponent(baseAmount, target);
    }
}
