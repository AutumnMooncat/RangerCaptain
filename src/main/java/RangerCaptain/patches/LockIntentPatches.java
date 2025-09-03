package RangerCaptain.patches;

import RangerCaptain.powers.BoobyTrappedPower;
import RangerCaptain.powers.SnowedInPower;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.IntentHelper;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.city.BronzeAutomaton;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.HexPower;
import javassist.*;
import javassist.bytecode.DuplicateMemberException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

import java.util.Arrays;

public class LockIntentPatches {
    @SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class LockedIntentField {
        public static SpireField<EnemyMoveInfo> lockedInfo = new SpireField<>(() -> null);
        public static SpireField<EnemyMoveInfo> desiredInfo = new SpireField<>(() -> null);
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "createIntent")
    public static class ThisIsJank {
        @SpirePrefixPatch
        public static void plz(AbstractMonster __instance) {
            EnemyMoveInfo lockedInfo = LockedIntentField.lockedInfo.get(__instance);
            if (lockedInfo != null) {
                EnemyMoveInfo currentInfo = IntentHelper.getMove(__instance);
                if (currentInfo != lockedInfo) {
                    IntentHelper.forceMove(__instance, lockedInfo);
                    AbstractPower tapeJam = __instance.getPower(TapeJamPower.POWER_ID);
                    if (tapeJam != null) {
                        tapeJam.flash();
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "setMove", paramtypez = {String.class, byte.class, AbstractMonster.Intent.class, int.class, int.class, boolean.class})
    public static class ThisIsVeryJank {
        @SpirePrefixPatch
        public static SpireReturn<Void> plz(AbstractMonster __instance, String moveName, byte nextMove, AbstractMonster.Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
            EnemyMoveInfo desiredMove = new EnemyMoveInfo(nextMove, intent, baseDamage, multiplier, isMultiDamage);
            if (IntentHelper.isNormalMove(desiredMove)) {
                LockedIntentField.desiredInfo.set(__instance, desiredMove);
            }
            AbstractPower tapeJam = __instance.getPower(TapeJamPower.POWER_ID);
            if (tapeJam != null) {
                tapeJam.flash();
                return SpireReturn.Return();
            }
            AbstractPower snowedIn = AbstractDungeon.player.getPower(SnowedInPower.POWER_ID);
            if (snowedIn != null && baseDamage > -1) {
                snowedIn.flash();
                IntentHelper.forceMove(__instance, new EnemyMoveInfo(nextMove, CustomIntentPatches.RANGER_SNOWED_IN, -1, 0, false));
                return SpireReturn.Return();
            }
            AbstractPower boobyTrap = __instance.getPower(BoobyTrappedPower.POWER_ID);
            if (boobyTrap != null) {
                IntentHelper.forceMove(__instance, new EnemyMoveInfo(nextMove, CustomIntentPatches.RANGER_BOMB, BoobyTrappedPower.BOOBY_TRAP_DAMAGE, 0, false));
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = BronzeAutomaton.class, method = SpirePatch.CLASS)
    public static class SpawnField {
        public static SpireField<BronzeOrb[]> leftOrbs = new SpireField<>(() -> new BronzeOrb[5]);
        public static SpireField<BronzeOrb[]> rightOrbs = new SpireField<>(() -> new BronzeOrb[5]);
    }

    public static boolean canSpawnLeftOrb(BronzeAutomaton instance) {
        return Arrays.stream(SpawnField.leftOrbs.get(instance)).anyMatch(orb -> orb == null || orb.isDeadOrEscaped());
    }

    public static boolean canSpawnRightOrb(BronzeAutomaton instance) {
        return Arrays.stream(SpawnField.rightOrbs.get(instance)).anyMatch(orb -> orb == null || orb.isDeadOrEscaped());
    }

    public static AbstractGameAction setLeftOrb(BronzeAutomaton instance, AbstractGameAction action) {
        if (action instanceof SpawnMonsterAction) {
            AbstractMonster m = ReflectionHacks.getPrivate(action, SpawnMonsterAction.class, "m");
            if (m instanceof BronzeOrb) {
                for (int i = 0 ; i < SpawnField.leftOrbs.get(instance).length; i++) {
                    if (SpawnField.leftOrbs.get(instance)[i] == null || SpawnField.leftOrbs.get(instance)[i].isDeadOrEscaped()) {
                        SpawnField.leftOrbs.get(instance)[i] = (BronzeOrb) m;
                        break;
                    }
                }
            }
        }
        return action;
    }

    public static AbstractGameAction setRightOrb(BronzeAutomaton instance, AbstractGameAction action) {
        if (action instanceof SpawnMonsterAction) {
            AbstractMonster m = ReflectionHacks.getPrivate(action, SpawnMonsterAction.class, "m");
            if (m instanceof BronzeOrb) {
                for (int i = 0 ; i < SpawnField.rightOrbs.get(instance).length; i++) {
                    if (SpawnField.rightOrbs.get(instance)[i] == null || SpawnField.rightOrbs.get(instance)[i].isDeadOrEscaped()) {
                        SpawnField.rightOrbs.get(instance)[i] = (BronzeOrb) m;
                        break;
                    }
                }
            }
        }
        return action;
    }

    public static final float[] leftXOffsets = new float[]{0, 0, 0, -160, -160};
    public static final float[] leftYOffsets = new float[]{0, 160, -160, 0, 160};
    public static final float[] rightXOffsets = new float[]{0, 0, 0, 160, 160};
    public static final float[] rightYOffsets = new float[]{0, 160, -160, 0, 160};

    public static float getXPos(BronzeAutomaton instance, float xPos, int count) {
        if (count == 0) {
            for (int i = 0 ; i < SpawnField.leftOrbs.get(instance).length ; i++) {
                if (SpawnField.leftOrbs.get(instance)[i] == null || SpawnField.leftOrbs.get(instance)[i].isDeadOrEscaped()) {
                    return xPos + leftXOffsets[i];
                }
            }
        } else {
            for (int i = 0 ; i < SpawnField.rightOrbs.get(instance).length ; i++) {
                if (SpawnField.rightOrbs.get(instance)[i] == null || SpawnField.rightOrbs.get(instance)[i].isDeadOrEscaped()) {
                    return xPos + rightXOffsets[i];
                }
            }
        }
        return xPos;
    }

    public static float getYPos(BronzeAutomaton instance, float yPos, int count) {
        if (count == 0) {
            for (int i = 0 ; i < SpawnField.leftOrbs.get(instance).length ; i++) {
                if (SpawnField.leftOrbs.get(instance)[i] == null || SpawnField.leftOrbs.get(instance)[i].isDeadOrEscaped()) {
                    return yPos + leftYOffsets[i];
                }
            }
        } else {
            for (int i = 0 ; i < SpawnField.rightOrbs.get(instance).length ; i++) {
                if (SpawnField.rightOrbs.get(instance)[i] == null || SpawnField.rightOrbs.get(instance)[i].isDeadOrEscaped()) {
                    return yPos + rightYOffsets[i];
                }
            }
        }
        return yPos;
    }

    @SpirePatch(clz = BronzeAutomaton.class, method = "takeTurn")
    public static class NegateActionsAndModifyOrbs {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                int hits = 0;

                @Override
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(BronzeOrb.class.getName())) {
                        e.replace(
                                "$1 = "+LockIntentPatches.class.getName()+".getXPos(this, $1, $3);" +
                                        "$2 = "+LockIntentPatches.class.getName()+".getYPos(this, $2, $3);" +
                                        "$_ = $proceed($$);"
                        );
                    }
                }

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (hits < 6 && m.getClassName().equals(GameActionManager.class.getName()) && m.getMethodName().equals("addToBottom")) {
                        switch (hits) {
                            case 0:
                            case 1:
                                m.replace("if ("+LockIntentPatches.class.getName()+".canSpawnLeftOrb(this)) {$proceed($$);}");
                                break;
                            case 2:
                                m.replace("if ("+LockIntentPatches.class.getName()+".canSpawnLeftOrb(this)) {$proceed("+LockIntentPatches.class.getName()+".setLeftOrb(this, $$));}");
                                break;
                            case 3:
                            case 4:
                                m.replace("if ("+LockIntentPatches.class.getName()+".canSpawnRightOrb(this)) {$proceed($$);}");
                                break;
                            case 5:
                                m.replace("if ("+LockIntentPatches.class.getName()+".canSpawnRightOrb(this)) {$proceed("+LockIntentPatches.class.getName()+".setRightOrb(this, $$));}");
                                break;
                        }
                        hits++;
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = HexPower.class, method = SpirePatch.CONSTRUCTOR)
    public static class HexDescription {
        @SpireRawPatch
        public static void plz(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass ctHexClass = ctBehavior.getDeclaringClass();

            CtMethod ctSuperMethod = ctHexClass.getSuperclass().getDeclaredMethod("updateDescription");
            CtMethod ctUpdateDescriptionMethod = CtNewMethod.delegator(ctSuperMethod, ctHexClass);
            ctUpdateDescriptionMethod.setBody("description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];");

            try {
                ctHexClass.addMethod(ctUpdateDescriptionMethod);
            } catch (DuplicateMemberException ignored) {
                //Surely if someone already patched a description onto it, I don't need to bother updating it
            }
        }
    }
}
