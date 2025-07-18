package RangerCaptain.cardmods.fusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.effects.LightningVFX;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.List;

public class LightningFVXComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(LightningFVXComponent.class.getSimpleName());

    public LightningFVXComponent() {
        super(ID, 0);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new LightningVFX();
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
        return new LightningFVXComponent();
    }
}
