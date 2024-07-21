package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ResonancePower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(ResonancePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int TRIGGER_DAMAGE = 25;

    public ResonancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void onInitialApplication() {
        checkStacks();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkStacks();
    }

    private void checkStacks() {
        if (this.amount >= 3) {
            addToBot(new DamageAction(this.owner, new DamageInfo(null, TRIGGER_DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));// 31
            addToBot(new ReducePowerAction(owner, owner, this, 3));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {// 48
            this.description = powerStrings.DESCRIPTIONS[0] + powerStrings.DESCRIPTIONS[1] + TRIGGER_DAMAGE + powerStrings.DESCRIPTIONS[3];// 49
        } else {
            this.description = powerStrings.DESCRIPTIONS[0] + powerStrings.DESCRIPTIONS[2] + TRIGGER_DAMAGE + powerStrings.DESCRIPTIONS[3];// 53
        }
    }
}