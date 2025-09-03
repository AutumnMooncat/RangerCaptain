package RangerCaptain.util;

import RangerCaptain.patches.CustomIntentPatches;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.lang.reflect.Field;

public class IntentHelper {
    private static final Field moveField;

    static {
        try {
            moveField = AbstractMonster.class.getDeclaredField("move");
            moveField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNormalMove(EnemyMoveInfo info) {
        return info.intent != CustomIntentPatches.RANGER_SNOWED_IN & info.intent != CustomIntentPatches.RANGER_BOMB;
    }

    public static EnemyMoveInfo getMove(AbstractMonster monster) {
        if (monster != null) {
            try {
                return (EnemyMoveInfo) moveField.get(monster);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static void forceMove(AbstractMonster monster, EnemyMoveInfo newMove) {
        if (monster != null) {
            byte moveByte = monster.nextMove;
            try {
                newMove.nextMove = moveByte;
                moveField.set(monster, newMove);
                monster.createIntent();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setMove(AbstractMonster monster, EnemyMoveInfo newMove) {
        if (monster != null) {
            monster.setMove(monster.nextMove, newMove.intent, newMove.baseDamage, newMove.multiplier, newMove.isMultiDamage);
            monster.createIntent();
        }
    }
}
