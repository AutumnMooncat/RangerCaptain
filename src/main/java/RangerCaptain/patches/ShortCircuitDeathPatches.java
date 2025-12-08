package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.*;
import javassist.bytecode.*;

import java.util.Arrays;

public class ShortCircuitDeathPatches {
    @SpirePatch(clz = AbstractCreature.class, method = SpirePatch.CLASS)
    public static class ShortCircuitField {
        public static SpireField<Boolean> shortCircuit = new SpireField<>(() -> false);
    }

    public static boolean instantKill(AbstractCreature c) {
        if (ShortCircuitField.shortCircuit.get(c)) {
            ShortCircuitField.shortCircuit.set(c, false);
            return true;
        }
        return false;
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "damage")
    public static class DiePlayer {
        @SpireRawPatch
        public static void youDontBelongInThisWorld(CtBehavior ctMethodToPatch) {
            try {
                ClassPool classPool = ctMethodToPatch.getDeclaringClass().getClassPool();
                MethodInfo mi = ctMethodToPatch.getMethodInfo();
                CodeAttribute ca = mi.getCodeAttribute();
                CodeIterator ci = ca.iterator();
                ConstPool cp = mi.getConstPool();
                CtClass scdpClazz = classPool.get(ShortCircuitDeathPatches.class.getName());
                CtClass acreClazz = classPool.get(AbstractCreature.class.getName());
                boolean insertCheck = false;
                boolean jumpCheck = false;
                boolean updatedBar = false;
                int insertIndex = -1;
                int jumpIndex = -1;
                int lastGetHealth = -1;
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
                        if (!insertCheck && cp.getFieldrefName(operand).equals("currentBlock")) {
                            insertCheck = true;
                            insertIndex = currentIndex;
                        }
                        if (!jumpCheck && cp.getFieldrefName(operand).equals("currentHealth")) {
                            lastGetHealth = currentIndex;
                        }
                    }
                    if (nextOpcode == Opcode.INVOKEVIRTUAL) {
                        int operand = ci.byteAt(next+1) << 8 | ci.byteAt(next+2);
                        if (cp.getMethodrefName(operand).equals("healthBarUpdatedEvent")) {
                            updatedBar = true;
                        }
                        if (!jumpCheck && updatedBar && cp.getMethodrefName(operand).equals("hasRelic")) {
                            jumpCheck = true;
                            jumpIndex = lastGetHealth;
                        }
                    }
                }
                if (insertIndex != -1 && jumpIndex != -1) {
                    MainModfile.logger.info("Found currentBlock and hasRelic(), inserting jump");
                    MainModfile.logger.info("Insert at: "+insertIndex+", Jump to: "+jumpIndex);
                    jumpIndex -= (insertIndex - 1); // We start at insert index
                    jumpIndex += 2; // 2 bytes added after jump
                    MainModfile.logger.info("Offset Jump to: "+jumpIndex);
                    byte j1 = (byte) (jumpIndex >> 8);
                    byte j2 = (byte) jumpIndex;

                    Bytecode code = new Bytecode(cp);
                    code.addAload(0);
                    code.addInvokestatic(scdpClazz, "instantKill", CtClass.booleanType, new CtClass[]{acreClazz});
                    code.addOpcode(Opcode.IFEQ);
                    code.addIndex(8);
                    code.addOpcode(Opcode.ICONST_0);
                    code.addOpcode(Opcode.ISTORE_3);
                    code.addOpcode(Opcode.GOTO);
                    code.add(j1);
                    code.add(j2);
                    MainModfile.logger.info("Constructed sequence: "+Arrays.toString(code.get()));
                    ci.insertAt(insertIndex, code.get());
                    mi.rebuildStackMap(classPool);

                }
            } catch (BadBytecode | NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        /*@SpireTranspilerPatch
        public static ArrayList<CodeInstruction> youDontBelongInThisWorld(TranspilerHandler handler) {
            try {
                ArrayList<CodeInstruction> codes = new ArrayList<>();
                MainModfile.logger.info("");
                MainModfile.logger.info("Transpiling on {}", handler.ctMethod);
                for (CodeInstruction instruction : handler.instructions) {
                    MainModfile.logger.info(instruction);
                }
                JumpLabel jump = handler.requestLabel();
                boolean jumpAdded = false;
                boolean labelAdded = false;
                CtMethod check = handler.classPool.getCtClass(ShortCircuitDeathPatches.class.getName()).getDeclaredMethod("instantKill");
                for (int i = 0; i < handler.instructions.size(); i++) {
                    CodeInstruction nextInstruction = handler.instructions.get(i);
                    if (!jumpAdded && i + 3 < handler.instructions.size() && handler.nextNOpcodes(i, Opcode.ILOAD_2, Opcode.IFGE, Opcode.ICONST_0, Opcode.ISTORE_2)) {
                        jumpAdded = true;
                        ArrayList<CodeInstruction> newCodes = new ArrayList<>();
                        newCodes.add(new CodeInstruction(Opcode.ALOAD_0));
                        newCodes.add(new CodeInstruction(Opcode.INVOKESTATIC, check));
                        newCodes.add(new CodeInstruction(Opcode.IFEQ, jump));
                        nextInstruction.moveLabels(newCodes.get(0));
                        codes.addAll(i, newCodes);
                    }
                    if (!labelAdded && i + 2 < handler.instructions.size() && handler.nextNOpcodes(i, Opcode.ALOAD_0, Opcode.GETFIELD, Opcode.ICONST_1)) {
                        Object operand = handler.instructions.get(i + 1).operand;
                        if (operand instanceof CtField) {
                            CtField field = (CtField) operand;
                            if (field.getName().equals("currentHealth")) {
                                labelAdded = true;
                                nextInstruction.addLabel(jump);
                            }
                        }
                    }
                    codes.add(nextInstruction);
                }
                return codes;
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }*/
    }

    //@SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class DieMonster {
        //@SpireRawPatch
        public static void youDontBelongInThisWorld(CtBehavior ctMethodToPatch) {
            try {
                ClassPool classPool = ctMethodToPatch.getDeclaringClass().getClassPool();
                MethodInfo mi = ctMethodToPatch.getMethodInfo();
                CodeAttribute ca = mi.getCodeAttribute();
                CodeIterator ci = ca.iterator();
                ConstPool cp = mi.getConstPool();
                CtClass scdpClazz = classPool.get(ShortCircuitDeathPatches.class.getName());
                CtClass boolClazz = classPool.get(Boolean.class.getName());
                CtClass acreClazz = classPool.get(AbstractCreature.class.getName());
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
                    code.addInvokestatic(scdpClazz, "instantKill", boolClazz, new CtClass[]{acreClazz});
                    code.addOpcode(Opcode.IFNE);
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
