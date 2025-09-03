package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.LockIntentPatches;
import RangerCaptain.util.IntentHelper;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TapeJamPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(TapeJamPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public TapeJamPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, true, owner, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster) {
            LockIntentPatches.LockedIntentField.lockedInfo.set(owner, IntentHelper.getMove((AbstractMonster) owner));
        }
    }

    @Override
    public void onRemove() {
        LockIntentPatches.LockedIntentField.lockedInfo.set(owner, null);
    }

    @Override
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }
}