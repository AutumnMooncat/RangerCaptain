package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.*;

import java.util.Arrays;

public class ShortCircuitDeathPatches {
    //@SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class ShortCircuitField {
        public static SpireField<Boolean> shortCircuit = new SpireField<>(() -> false);
    }

    public static boolean circuitCheck(AbstractMonster mon) {
        return !ShortCircuitField.shortCircuit.get(mon);
    }

    //@SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class DieMonster {
        @SpireRawPatch
        public static void youDontBelongInThisWorld(CtBehavior ctMethodToPatch) {
            try {
                ClassPool classPool = ctMethodToPatch.getDeclaringClass().getClassPool();
                MethodInfo mi = ctMethodToPatch.getMethodInfo();
                CodeAttribute ca = mi.getCodeAttribute();
                CodeIterator ci = ca.iterator();
                ConstPool cp = mi.getConstPool();
                CtClass scdpClazz = classPool.get(ShortCircuitDeathPatches.class.getName());
                CtClass boolClazz = classPool.get(Boolean.class.getName());
                CtClass amonClazz = classPool.get(AbstractMonster.class.getName());
                boolean insertCheck = false;
                boolean jumpCheck = false;
                int insertIndex = -1;
                int jumpIndex = -1;
                while (ci.hasNext()) {
                    int currentIndex = ci.next();
                    int currentOpcode = ci.byteAt(currentIndex);
                    if (!ci.hasNext()) {
                        break;
                    }
                    int next = ci.lookAhead();
                    int nextOpcode = ci.byteAt(next);
                    //MainModfile.logger.info(Mnemonic.OPCODE[nextOpcode]);
                    if (nextOpcode == Opcode.GETFIELD) {
                        int operand = ci.byteAt(next+1) << 8 | ci.byteAt(next+2);
                        //MainModfile.logger.info("Found field access: {}.{} ({})", cp.getFieldrefClassName(operand), cp.getFieldrefName(operand), cp.getFieldrefType(operand));
                        if (!insertCheck && cp.getFieldrefName(operand).equals("isDying")) {
                            insertCheck = true;
                            insertIndex = currentIndex;
                        }
                    }
                    if (nextOpcode == Opcode.INVOKEVIRTUAL) {
                        int operand = ci.byteAt(next+1) << 8 | ci.byteAt(next+2);
                        if (!jumpCheck && cp.getMethodrefName(operand).equals("die")) {
                            jumpCheck = true;
                            jumpIndex = currentIndex;
                        }
                    }
                }
                if (insertIndex != -1 && jumpIndex != -1) {
                    MainModfile.logger.info("Found isDying and die(), inserting jump");
                    MainModfile.logger.info("Insert at: "+insertIndex+", Jump to: "+jumpIndex);
                    jumpIndex -= (insertIndex - 1); // We start at insert index
                    jumpIndex += 2; // 2 bytes added after jump
                    MainModfile.logger.info("Offset Jump to: "+jumpIndex);
                    byte j1 = (byte) (jumpIndex >> 8);
                    byte j2 = (byte) jumpIndex;

                    Bytecode code = new Bytecode(cp);
                    code.addAload(0);
                    code.addInvokestatic(scdpClazz, "circuitCheck", boolClazz, new CtClass[]{amonClazz});
                    code.addOpcode(Opcode.IFEQ);
                    code.add(j1);
                    code.add(j2);
                    MainModfile.logger.info("Constructed sequence: "+Arrays.toString(code.get()));
                    ci.insertAt(insertIndex, code.get());
                }
            } catch (BadBytecode | NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
