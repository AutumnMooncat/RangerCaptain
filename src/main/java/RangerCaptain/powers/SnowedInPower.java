package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.patches.CustomIntentPatches;
import RangerCaptain.patches.LockIntentPatches;
import RangerCaptain.util.IntentHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

public class SnowedInPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SnowedInPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean shouldRevert = true;

    public SnowedInPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, true, owner, amount);
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
    public boolean canPlayCard(AbstractCard card) {
        return card.type != AbstractCard.CardType.ATTACK;
    }

    @Override
    public void onRemove() {
        addToTop(new DoAction(() -> {
            Wiz.forAllMonstersLiving(mon -> {
                if (shouldRevert) {
                    EnemyMoveInfo replaced = LockIntentPatches.LockedIntentField.desiredInfo.get(mon);
                    if (replaced != null) {
                        IntentHelper.setMove(mon, replaced);
                    }
                }
            });
        }));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        shouldRevert = false;
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public void onInitialApplication() {
        addToBot(new DoAction(() -> {
            Wiz.forAllMonstersLiving(mon -> {
                if (mon.getIntentBaseDmg() > -1) {
                    EnemyMoveInfo current = IntentHelper.getMove(mon);
                    if (IntentHelper.isNormalMove(current)) {
                        LockIntentPatches.LockedIntentField.desiredInfo.set(mon, current);
                    }
                    // Attempt force move, Tape Jam will prevent if it exists
                    IntentHelper.forceMove(mon, new EnemyMoveInfo(mon.nextMove, CustomIntentPatches.RANGER_SNOWED_IN, -1, 0, false));
                }
            });
        }));
    }
}