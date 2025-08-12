package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.BurnToAshEffect;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class ExplodeAllVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(ExplodeAllVFXComponent.class.getSimpleName());

    public ExplodeAllVFXComponent() {
        super(ID, ComponentTarget.NONE);
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        addToBot(new DoAction(() -> Wiz.forAllMonstersLiving(mon -> {
            AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(mon.hb.cX, mon.hb.cY));
            AbstractDungeon.effectsQueue.add(new BurnToAshEffect(mon.hb.cX, mon.hb.cY));
        })));
        addToBot(new ShakeScreenAction(0f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ExplodeAllVFXComponent();
    }
}
