package RangerCaptain.cardfusion.abstracts;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public abstract class AbstractTraitComponent extends AbstractComponent {

    public AbstractTraitComponent(String ID) {
        super(ID, 0, ComponentType.TRAIT, ComponentTarget.NONE, DynVar.NONE);
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return identifier().equals(other.identifier());
    }

    @Override
    public String rawCapturedText() {
        return null;
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) { }
}
