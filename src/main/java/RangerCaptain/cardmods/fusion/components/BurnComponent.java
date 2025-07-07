package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BurnComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(BurnComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public BurnComponent(int base) {
        this(base, ComponentTarget.ENEMY);
    }

    public BurnComponent(int base, ComponentTarget target) {
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
        return new BurnedPower(target, Wiz.adp(), amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BurnComponent(baseAmount, target);
    }
}
