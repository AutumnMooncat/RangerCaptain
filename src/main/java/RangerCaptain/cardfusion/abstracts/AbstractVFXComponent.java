package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public abstract class AbstractVFXComponent extends AbstractComponent {

    public AbstractVFXComponent(String ID, ComponentTarget target) {
        super(ID, 0, ComponentType.DO, target, DynVar.NONE);
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return identifier().equals(other.identifier());
    }

    @Override
    public void updatePrio() {
        this.priority = DAMAGE_PRIO;
    }

    @Override
    public String componentDescription() {
        return null;
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return null;
    }

    @Override
    public String rawCapturedText() {
        return null;
    }

    public abstract void playVFX(AbstractCreature source, AbstractCreature target);

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        switch (target) {
            case SELF:
                playVFX(p, p);
                break;
            case ENEMY:
            case ENEMY_RANDOM:
            case NONE:
                playVFX(p, m);
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> playVFX(p, mon));
                break;
        }
    }
}
