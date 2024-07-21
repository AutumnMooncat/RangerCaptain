package RangerCaptain.util;

import RangerCaptain.util.matcher.BasicMatchChecker;
import RangerCaptain.util.matcher.CompoundMatcher;
import basemod.Pair;
import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomCard;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ArchetypeHelper {
    // Keep track of checked classes for later. This is similar in weight to just making static spirefields on every class.
    private static final HashMap<Class<?>, HashMap<Matcher[], Boolean>> performedClassChecks = new HashMap<>();
    // If we end up in these classes, we know we have gone too far and can stop method call recursion
    public static Class<?>[] bannedMethodChecks = {AbstractDungeon.class, GameActionManager.class, AbstractPlayer.class, AbstractCreature.class, AbstractMonster.class, SpriteBatch.class, FontHelper.class, AbstractCard.class, CustomCard.class};
    // These classes create false positives as they are accidentally included in basegame cards
    public static Class<?>[] bannedClassChecks = {ExhaustAllEtherealAction.class};
    // We want to check objects which extend these
    public static Class<?>[] importantSuperClasses = {AbstractGameAction.class, AbstractPower.class, AbstractOrb.class, AbstractDamageModifier.class, AbstractCardModifier.class};
    // Built from previous array when needed
    public static final ArrayList<CtClass> importantCtSuperClasses = new ArrayList<>();
    // Don't look at methods from classes that start with these
    public static final String[] bannedPaths = {"java", "basemod", "com.badlogic"};
    // Keeps track of traversed methods when testing a card
    public static ArrayList<Pair<CtClass, CtMethod>> methodStack = new ArrayList<>();

    // Matchers to check for, used by givesBlock
    public static Matcher[] blockMatchers = {
            new Matcher.MethodCallMatcher(AbstractCreature.class, "addBlock"),
            new Matcher.MethodCallMatcher(AbstractPlayer.class, "addBlock")
    };

    public static boolean givesBlock(AbstractCard card) {
        //Easy out if the card has a baseBlock
        if (card.baseBlock > -1 && !(card instanceof RitualDagger)) {
            return true;
        }
        //Else we need to recursively check everything it does for blockMatchers
        return testCard(card, blockMatchers);
    }

    public static boolean testCard(AbstractCard card, Matcher... matchers) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        classes.add(card.getClass());
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            classes.add(mod.getClass());
        }
        for (AbstractDamageModifier dmod : DamageModifierManager.modifiers(card)) {
            classes.add(dmod.getClass());
        }
        ClassPool pool = Loader.getClassPool();
        if (importantCtSuperClasses.isEmpty()) {
            for (Class<?> clazz : importantSuperClasses) {
                try {
                    importantCtSuperClasses.add(pool.get(clazz.getName()));
                } catch (NotFoundException ignored) {}
            }
        }
        for (Class<?> clazz : classes) {
            if (!performedClassChecks.containsKey(clazz)) {
                performedClassChecks.put(clazz, new HashMap<>());
            }
            if (performedClassChecks.get(clazz).containsKey(matchers)) {
                if (performedClassChecks.get(clazz).get(matchers)) {
                    return true;
                }
            } else {
                try {
                    CtClass ctClass = pool.getCtClass(clazz.getName());
                    methodStack.clear();
                    if (classTester(ctClass, matchers)) {
                        performedClassChecks.get(clazz).put(matchers, true);
                        return true;
                    }
                } catch (NotFoundException ignored) {}
                performedClassChecks.get(clazz).put(matchers, false);
            }
        }
        return false;
    }

    public static boolean classTester(CtClass ctClass, Matcher... matchers) {
        ArrayList<CtMethod> foundMethods = methodFinder(ctClass);
        for (CtMethod currentMethod : foundMethods) {
            if (methodTester(currentMethod, matchers)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<CtMethod> methodFinder(CtClass ctClass) {
        ctClass.defrost();
        ArrayList<CtMethod> foundMethods = new ArrayList<>(Arrays.asList(ctClass.getDeclaredMethods()));
        if (importantCtSuperClasses.stream().anyMatch(ctc -> ctClass.subclassOf(ctc) && !ctc.getClassFile2().getName().equals(AbstractOrb.class.getName()))) {
            CtClass currentCtClass = ctClass;
            while (true) {
                try {
                    CtClass superCtClass = currentCtClass.getSuperclass();
                    for (CtMethod m : superCtClass.getDeclaredMethods()) {
                        if (Arrays.stream(bannedMethodChecks).noneMatch(clazz -> m.getDeclaringClass().getName().equals(clazz.getName()))) {
                            if (Arrays.stream(bannedPaths).noneMatch(s -> m.getDeclaringClass().getName().startsWith(s))) {
                                foundMethods.add(m);
                            }
                        }
                    }
                    currentCtClass = superCtClass;
                } catch (Exception ignored) {
                    break;
                }
            }
        }
        return foundMethods;
    }

    public static boolean methodTester(CtMethod check, Matcher... matchers) {
        Pair<CtClass, CtMethod> key = new Pair<>(check.getDeclaringClass(), check);
        if (runTest(check, matchers)) {
            return true;
        }
        if (!methodStack.contains(key)) {
            methodStack.add(key);
            for (CtMethod next : getReferencedMethods(check)) {
                if (methodTester(next, matchers)) {
                    return true;
                }
            }
            for (CtClass next : getReferencedClasses(check)) {
                if (classTester(next, matchers)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean runTest(CtMethod ctMethodToPatch, Matcher... matchers) {
        for (Matcher matcher : matchers) {
            if (matcher instanceof CompoundMatcher) {
                ((CompoundMatcher) matcher).reset();
            }
            BasicMatchChecker editor = new BasicMatchChecker(matcher);
            try {
                ctMethodToPatch.getDeclaringClass().defrost();
                ctMethodToPatch.instrument(editor);// 24
                if (editor.didMatch()) {
                    return true;
                }
            } catch (CannotCompileException | RuntimeException ignored) {}
        }
        return false;
    }

    public static ArrayList<CtMethod> getReferencedMethods(CtMethod method) {
        ArrayList<CtMethod> foundMethods = new ArrayList<>();
        try {
            method.getDeclaringClass().defrost();
            method.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) {
                    try {
                        CtMethod newMethod = m.getMethod();
                        if (Arrays.stream(bannedMethodChecks).noneMatch(clazz -> newMethod.getDeclaringClass().getName().equals(clazz.getName()))) {
                            if (Arrays.stream(bannedPaths).noneMatch(s -> newMethod.getDeclaringClass().getName().startsWith(s))) {
                                foundMethods.add(newMethod);
                            }
                        }
                    } catch (NotFoundException | RuntimeException ignored) {}
                }
            });
        } catch (CannotCompileException ignored) {}
        return foundMethods;
    }

    public static ArrayList<CtClass> getReferencedClasses(CtMethod method) {
        ArrayList<CtClass> foundClasses = new ArrayList<>();
        try {
            method.getDeclaringClass().defrost();
            method.instrument(new ExprEditor() {
                @Override
                public void edit(NewExpr e) {
                    try {
                        CtClass ctExprClass = e.getConstructor().getDeclaringClass();
                        if (Arrays.stream(bannedClassChecks).noneMatch(clazz -> ctExprClass.getClassFile2().getName().equals(clazz.getName()))) {
                            if (importantCtSuperClasses.stream().anyMatch(ctExprClass::subclassOf)) {
                                foundClasses.add(ctExprClass);
                            }
                        }
                    } catch (NotFoundException | RuntimeException ignored) {}
                }
            });
        } catch (CannotCompileException ignored) {}
        return foundClasses;
    }
}
