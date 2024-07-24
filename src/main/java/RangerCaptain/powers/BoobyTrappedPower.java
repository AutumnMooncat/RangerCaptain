package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.IntentPatches;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.lang.reflect.Field;

public class BoobyTrappedPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(BoobyTrappedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final EnemyMoveInfo boobyTrapMove = new EnemyMoveInfo((byte) -1, IntentPatches.RANGER_BOMB, 15, 0, false);
    private static final Field moveField;

    static {
        try {
            moveField = AbstractMonster.class.getDeclaredField("move");
            moveField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
    public void onInitialApplication() {
        setMove();
    }

    private EnemyMoveInfo getMove() {
        if (owner instanceof AbstractMonster) {
            try {
                return (EnemyMoveInfo) moveField.get(owner);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void setMove() {
        if (owner instanceof AbstractMonster) {
            byte moveByte = ((AbstractMonster)owner).nextMove;
            try {
                boobyTrapMove.nextMove = moveByte;
                moveField.set(owner, boobyTrapMove);
                ((AbstractMonster)owner).createIntent();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}