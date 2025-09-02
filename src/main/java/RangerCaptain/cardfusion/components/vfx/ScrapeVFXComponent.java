package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.effects.ScrapeVFX;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;

import java.util.List;

public class ScrapeVFXComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(ScrapeVFXComponent.class.getSimpleName());

    public ScrapeVFXComponent() {
        super(ID, 0);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new ScrapeVFX();
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
        return new ScrapeVFXComponent();
    }
}
