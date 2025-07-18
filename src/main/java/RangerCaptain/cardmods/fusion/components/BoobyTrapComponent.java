package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractSimpleApplyComponent;
import RangerCaptain.powers.BoobyTrappedPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class BoobyTrapComponent extends AbstractSimpleApplyComponent {
    public static final String ID = MainModfile.makeID(BoobyTrapComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int BASE_DAMAGE = 50;

    public BoobyTrapComponent(int base) {
        this(base, ComponentTarget.ENEMY);
    }

    public BoobyTrapComponent(int base, ComponentTarget target) {
        super(ID, base, target);
        dynvar = DynVar.FLAT;
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
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(BASE_CARD_TEXT[target.ordinal()], BASE_DAMAGE/baseAmount, getKeyword());
    }

    @Override
    public String rawCapturedText() {
        return String.format(BASE_CARD_TEXT[ComponentTarget.values().length], BASE_DAMAGE/baseAmount, getKeyword());
    }

    @Override
    public AbstractPower getPower(AbstractCreature target, int amount) {
        return new BoobyTrappedPower(target, BASE_DAMAGE/amount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BoobyTrapComponent(baseAmount, target);
    }
}
