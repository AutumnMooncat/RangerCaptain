package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.powers.TapeJamPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TapeJamComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(TapeJamComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public TapeJamComponent(int base) {
        this(base, ComponentTarget.ENEMY);
    }

    public TapeJamComponent(int base, ComponentTarget target) {
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
        return new TapeJamPower(target, amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new TapeJamComponent(baseAmount, target);
    }
}
