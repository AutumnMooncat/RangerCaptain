package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

public class InflameVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(InflameVFXComponent.class.getSimpleName());

    public InflameVFXComponent() {
        super(ID, ComponentTarget.NONE);
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        addToBot(new VFXAction(source, new InflameEffect(source), 0.1F));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new InflameVFXComponent();
    }
}
