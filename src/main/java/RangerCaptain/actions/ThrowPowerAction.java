package RangerCaptain.actions;

import RangerCaptain.util.TextureSniper;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.VFXContainer;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ThrowPowerAction extends AbstractGameAction {
    private final AbstractPower power;

    public ThrowPowerAction(AbstractCreature m, AbstractPower power) {
        this.target = m;
        this.power = power;
        this.source = Wiz.adp();
    }

    @Override
    public void update() {
        addToTop(new ApplyPowerAction(target, source, power));
        addToTop(new VFXAction(VFXContainer.throwEffect(TextureSniper.snipePower(power), 2f, target.hb, Color.FIREBRICK, false, true)));
        this.isDone = true;
    }
}
