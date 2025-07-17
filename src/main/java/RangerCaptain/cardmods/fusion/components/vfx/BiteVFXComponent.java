package RangerCaptain.cardmods.fusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.effects.BiteVFX;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.List;

public class BiteVFXComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(BiteVFXComponent.class.getSimpleName());

    public BiteVFXComponent() {
        super(ID, 0);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new BiteVFX();
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
        return rawCardText(null);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BiteVFXComponent();
    }
}
