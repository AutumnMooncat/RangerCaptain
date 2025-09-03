package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.patches.CustomIntentPatches;
import RangerCaptain.patches.LockIntentPatches;
import RangerCaptain.util.IntentHelper;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

public class BoobyTrappedPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(BoobyTrappedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final int BOOBY_TRAP_DAMAGE = 15;
    private boolean shouldRevert = true;

    public BoobyTrappedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn() {
        shouldRevert = false;
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public void onRemove() {
        addToTop(new DoAction(() -> {
            if (owner instanceof AbstractMonster) {
                if (shouldRevert) {
                    EnemyMoveInfo replaced = LockIntentPatches.LockedIntentField.desiredInfo.get(owner);
                    if (replaced != null) {
                        IntentHelper.setMove((AbstractMonster) owner, replaced);
                    }
                }
            }
        }));
    }

    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster) {
            addToBot(new DoAction(() -> {
                EnemyMoveInfo current = IntentHelper.getMove((AbstractMonster) owner);
                if (IntentHelper.isNormalMove(current)) {
                    LockIntentPatches.LockedIntentField.desiredInfo.set(owner, current);
                }
                IntentHelper.setMove((AbstractMonster) owner, new EnemyMoveInfo((byte) -1, CustomIntentPatches.RANGER_BOMB, BOOBY_TRAP_DAMAGE, 0, false));
            }));
        }
    }
}