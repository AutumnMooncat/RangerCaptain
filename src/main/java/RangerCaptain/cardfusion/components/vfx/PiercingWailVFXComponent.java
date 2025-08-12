package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class PiercingWailVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(PiercingWailVFXComponent.class.getSimpleName());

    public PiercingWailVFXComponent() {
        super(ID, ComponentTarget.NONE);
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
        addToBot(new VFXAction(source, new ShockWaveEffect(source.hb.cX, source.hb.cY, Settings.PURPLE_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new PiercingWailVFXComponent();
    }
}
