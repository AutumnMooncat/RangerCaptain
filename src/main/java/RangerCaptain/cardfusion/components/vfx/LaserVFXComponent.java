package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class LaserVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(LaserVFXComponent.class.getSimpleName());

    public LaserVFXComponent() {
        super(ID, ComponentTarget.NONE);
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        if (source != null && target != null) {
            addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
            addToBot(new VFXAction(new SmallLaserEffect(source.hb.cX, source.hb.cY, target.hb.cX, target.hb.cY), 0.1F));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new LaserVFXComponent();
    }
}
