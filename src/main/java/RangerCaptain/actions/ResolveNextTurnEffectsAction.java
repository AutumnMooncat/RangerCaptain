package RangerCaptain.actions;

import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.powers.NextTurnDamagePower;
import RangerCaptain.powers.NextTurnPowerLaterPower;
import RangerCaptain.powers.NextTurnPowerPower;
import RangerCaptain.powers.NextTurnTakeDamagePower;
import RangerCaptain.powers.interfaces.MonsterAtPlayerStartOfTurnPower;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ResolveNextTurnEffectsAction extends AbstractGameAction {
    private static final Class<?>[] knownClasses = {NextTurnBlockPower.class, NextTurnDamagePower.class, NextTurnTakeDamagePower.class, NextTurnPowerPower.class, NextTurnPowerLaterPower.class, DrawCardNextTurnPower.class, EndTurnDeathPower.class, EnergizedPower.class, EnergizedBluePower.class};
    private static final String[] wantedMethods = {"atStartOfTurn", "atPlayerStartOfTurn", "onEnergyRecharge", "atStartOfTurnPostDraw"};
    public static final String[] permissibleMethods = {"updateDescription", "makeCopy", "stackPower"};
    private static final HashMap<Class<?>, Boolean> processed = new HashMap<>();

    public ResolveNextTurnEffectsAction() {
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        ActionCapturePatch.doCapture = true;
        ArrayList<AbstractPower> validPlayerPowers = AbstractDungeon.player.powers.stream().filter(this::analyzePower).collect(Collectors.toCollection(ArrayList::new));
        for (AbstractPower power : validPlayerPowers) {
            power.atStartOfTurn();
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                for (AbstractPower power : monster.powers) {
                    if (power instanceof MonsterAtPlayerStartOfTurnPower && analyzePower(power)) {
                        ((MonsterAtPlayerStartOfTurnPower) power).atPlayerStartOfTurn();
                    }
                }
            }
        }
        for (AbstractPower power : validPlayerPowers) {
            power.onEnergyRecharge();
        }
        for (AbstractPower power : validPlayerPowers) {
            power.atStartOfTurnPostDraw();
        }
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                for (AbstractPower power : monster.powers) {
                    if (analyzePower(power)) {
                        power.atStartOfTurn();
                    }
                }
            }
        }
        ActionCapturePatch.releaseToTop();
        this.isDone = true;
    }

    private boolean analyzePower(AbstractPower power) {
        if (Arrays.stream(knownClasses).anyMatch(clz -> clz == power.getClass())) {
            return true;
        }

        if (processed.containsKey(power.getClass())) {
            return processed.get(power.getClass());
        }

        try {
            //Grab the ctclass of the power
            CtClass ctPower = Loader.getClassPool().get(power.getClass().getName());
            int goodMethods = 0;
            int badMethods = 0;
            boolean[] callsRemove = {false};
            //Iterate the power's declared methods
            //We care that it overrides exactly one of the three next turn triggers and no other hooks
            for (CtMethod ctm : ctPower.getDeclaredMethods()) {
                // Ignore static methods as they are likely helpers
                if ((ctm.getMethodInfo2().getAccessFlags() & AccessFlag.STATIC) == AccessFlag.STATIC) {
                    continue;
                }
                if (ctm.getName().equals("atStartOfTurnPostDraw") || ctm.getName().equals("atStartOfTurn") || ctm.getName().equals("onEnergyRecharge")) {
                    goodMethods++;
                    ctPower.defrost();
                    ctm.instrument(new ExprEditor() {
                        @Override
                        public void edit(NewExpr e) {
                            if (e.getClassName().equals(RemoveSpecificPowerAction.class.getName())) {
                                callsRemove[0] = true;
                            }
                        }
                    });
                } else if (!ctm.getName().equals("updateDescription") && !ctm.getName().equals("makeCopy") && !ctm.getName().equals("stackPower")) {
                    badMethods++;
                }
            }
            //We probably have a match
            if (goodMethods == 1 && badMethods == 0 && callsRemove[0]) {
                processed.put(power.getClass(), true);
                return true;
            }
        } catch (NotFoundException | CannotCompileException ignored) {}
        processed.put(power.getClass(), false);
        return false;
    }
}
