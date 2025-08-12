package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;

public class DieDieDieVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(DieDieDieVFXComponent.class.getSimpleName());

    public DieDieDieVFXComponent() {
        super(ID, ComponentTarget.NONE);
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        addToBot(new VFXAction(new BorderLongFlashEffect(Color.LIGHT_GRAY)));
        addToBot(new VFXAction(new DieDieDieEffect(), 0.7F));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DieDieDieVFXComponent();
    }
}
