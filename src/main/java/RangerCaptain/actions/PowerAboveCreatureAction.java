package RangerCaptain.actions;

import RangerCaptain.vfx.PowerAboveCreatureEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerAboveCreatureAction extends AbstractGameAction {
    private boolean used = false;
    private final AbstractPower power;

    public PowerAboveCreatureAction(AbstractCreature source, AbstractPower power) {
        this.setValues(source, source);
        this.power = power;
        this.actionType = ActionType.TEXT;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public void update() {
        if (!used) {
            AbstractDungeon.effectList.add(new PowerAboveCreatureEffect(source.hb.cX - source.animX, source.hb.cY + source.hb.height / 2.0F - source.animY, power));
            used = true;
        }

        tickDuration();
    }
}
