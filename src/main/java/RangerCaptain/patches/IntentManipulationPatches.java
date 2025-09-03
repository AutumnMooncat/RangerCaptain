package RangerCaptain.patches;

import RangerCaptain.actions.DoAction;
import RangerCaptain.util.IntentHelper;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.BigExplosionVFX;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.Iterator;

public class IntentManipulationPatches {

    public static void doSnowedIn(AbstractMonster monster) {
        rollOrBackupMove(monster);
    }

    public static void doBoobyTrap(AbstractMonster monster) {
        Wiz.atb(new AnimateShakeAction(monster, 1.0f, 0.3f));
        Wiz.atb(new BigExplosionVFX(monster, ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT));
        Wiz.atb(new DamageAction(AbstractDungeon.player, new DamageInfo(monster, ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentDmg"), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE, true));
        Wiz.forAllMonstersLiving(mon -> {
            if (mon != monster) {
                Wiz.atb(new DamageAction(mon, new DamageInfo(monster, calculateDamage(monster, mon, ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentBaseDmg")), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE, true));
            }
        });
        Wiz.atb(new DamageAction(monster, new DamageInfo(monster, calculateDamage(monster, monster, ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentBaseDmg")), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE, true));
        rollOrBackupMove(monster);
    }

    private static void rollOrBackupMove(AbstractMonster monster) {
        Wiz.atb(new DoAction(() -> LockIntentPatches.LockedIntentField.successfullyRolledMove.set(monster, false)));
        Wiz.atb(new RollMoveAction(monster));
        Wiz.atb(new DoAction(() -> {
            if (!LockIntentPatches.LockedIntentField.successfullyRolledMove.get(monster)) {
                EnemyMoveInfo backup = LockIntentPatches.LockedIntentField.desiredInfo.get(monster);
                if (backup != null) {
                    IntentHelper.setMove(monster, backup);
                }
            }
        }));
    }

    private static int calculateDamage(AbstractCreature source, AbstractCreature target, int dmg) {
        float tmp = (float)dmg;
        if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
            float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
            tmp *= mod;
        }

        AbstractPower p;
        Iterator<AbstractPower> iterator;
        for(iterator = source.powers.iterator(); iterator.hasNext(); tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = iterator.next();
        }

        for(iterator = target.powers.iterator(); iterator.hasNext(); tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = iterator.next();
        }

        for(iterator = source.powers.iterator(); iterator.hasNext(); tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = iterator.next();
        }

        for(iterator = target.powers.iterator(); iterator.hasNext(); tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL)) {
            p = iterator.next();
        }

        dmg = MathUtils.floor(tmp);
        if (dmg < 0) {
            dmg = 0;
        }

        return dmg;
    }

    @SpirePatch(clz = GameActionManager.class, method = "getNextAction")
    public static class GetNextAction {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractMonster.class.getName()) && m.getMethodName().equals("takeTurn")) {
                        m.replace("if (m.intent == "+CustomIntentPatches.class.getName()+".RANGER_SNOWED_IN) {"+ IntentManipulationPatches.class.getName()+".doSnowedIn(m);} else if (m.intent == "+CustomIntentPatches.class.getName()+".RANGER_BOMB) {"+ IntentManipulationPatches.class.getName()+".doBoobyTrap(m);} else {$_ = $proceed($$);}");
                    }
                }
            };
        }
    }
}
