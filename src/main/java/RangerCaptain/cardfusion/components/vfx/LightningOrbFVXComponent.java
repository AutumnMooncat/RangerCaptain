package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.effects.LightningOrbVFX;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.List;

public class LightningOrbFVXComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(LightningOrbFVXComponent.class.getSimpleName());

    public LightningOrbFVXComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new LightningOrbVFX();
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

    @Override
    public AbstractComponent makeCopy() {
        return new LightningOrbFVXComponent();
    }
}
