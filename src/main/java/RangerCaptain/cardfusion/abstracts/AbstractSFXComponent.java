package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public abstract class AbstractSFXComponent extends AbstractComponent {
    public static final float COOLDOWN = 0.05f;

    public AbstractSFXComponent(String ID) {
        super(ID, 0, ComponentType.DO, ComponentTarget.NONE, DynVar.NONE);
        MainModfile.sfxCooldowns.put(ID, COOLDOWN);
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

    public abstract void playSFX();

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        if (MainModfile.sfxCooldowns.get(identifier()) <= 0f) {
            playSFX();
            MainModfile.sfxCooldowns.put(identifier(), COOLDOWN);
        }
    }
}
