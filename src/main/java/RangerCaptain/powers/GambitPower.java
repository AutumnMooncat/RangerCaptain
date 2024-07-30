package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class GambitPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(GambitPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GambitPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, true, owner, amount);
        priority = 7;
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0];
        } else {
            description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 2.0F : damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return blockAmount * 2;
    }

    @Override
    public void atStartOfTurn() {
        flash();
        if (amount == 1) {
            addToBot(new VFXAction(new LightningEffect(this.owner.hb.cX, this.owner.hb.cY)));
            addToBot(new LoseHPAction(this.owner, this.owner, 99999));
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }

    }
}