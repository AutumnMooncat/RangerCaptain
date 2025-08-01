package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpireInitializer
public class CardInHandSuite implements
        OnCardUseSubscriber,
        OnPlayerDamagedSubscriber,
        OnPlayerLoseBlockSubscriber,
        PostDrawSubscriber,
        PostExhaustSubscriber,
        PostPowerApplySubscriber {

    public CardInHandSuite() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        CardInHandSuite suite = new CardInHandSuite();
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onCardUsed(abstractCard);
            }
        }
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onDamaged(damageInfo);
            }
        }
        return i;
    }

    @Override
    public int receiveOnPlayerLoseBlock(int i) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onStartTurnBlockLoss(i);
            }
        }
        return i;
    }

    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onCardDrawn(abstractCard);
            }
        }
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onCardExhausted(abstractCard);
            }
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onPowerApplied(power, target, source);
            }
        }
    }

    public static float atDamageGive(float dmg, DamageInfo.DamageType type, AbstractCreature target, AbstractCard cardAffected) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                dmg = ((InHandCard) card).atDamageGive(dmg, type, target, cardAffected);
            }
        }
        return dmg;
    }

    public static float atDamageFinalGive(float dmg, DamageInfo.DamageType type, AbstractCreature target, AbstractCard cardAffected) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                dmg = ((InHandCard) card).atDamageFinalGive(dmg, type, target, cardAffected);
            }
        }
        return dmg;
    }

    public static float onModifyBlock(float block, AbstractCard cardAffected) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                block = ((InHandCard) card).onModifyBlock(block, cardAffected);
            }
        }
        return block;
    }

    public static float onModifyBlockFinal(float block, AbstractCard cardAffected) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                block = ((InHandCard) card).onModifyBlockFinal(block, cardAffected);
            }
        }
        return block;
    }

    public static void onGainBlock(int block) {
        for (AbstractCard card : Wiz.adp().hand.group) {
            if (card instanceof InHandCard) {
                ((InHandCard) card).onGainBlock(block);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class ModifyDamageCCD {
        @SpireInsertPatch(locator = DamageLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = atDamageGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = MultiDamageLocator.class, localvars = {"tmp","i","m"})
        public static void multi(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i, ArrayList<AbstractMonster> m) {
            tmp[i] = atDamageGive(tmp[i], __instance.damageTypeForTurn, m.get(i), __instance);
        }

        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = "tmp")
        public static void singleFinal(AbstractCard __instance, AbstractMonster mo, @ByRef float[] tmp) {
            tmp[0] = atDamageFinalGive(tmp[0], __instance.damageTypeForTurn, mo, __instance);
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp","i","m"})
        public static void multiFinal(AbstractCard __instance, AbstractMonster mo, float[] tmp, int i, ArrayList<AbstractMonster> m) {
            tmp[i] = atDamageFinalGive(tmp[i], __instance.damageTypeForTurn, m.get(i), __instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ModifyDamageAP {
        @SpireInsertPatch(locator = DamageLocator.class, localvars = "tmp")
        public static void single(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = atDamageGive(tmp[0], __instance.damageTypeForTurn, null, __instance);
        }

        @SpireInsertPatch(locator = MultiDamageLocator.class, localvars = {"tmp","i"})
        public static void multi(AbstractCard __instance, float[] tmp, int i) {
            tmp[i] = atDamageGive(tmp[i], __instance.damageTypeForTurn, null, __instance);
        }

        @SpireInsertPatch(locator = DamageFinalLocator.class, localvars = "tmp")
        public static void singleFinal(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = atDamageFinalGive(tmp[0], __instance.damageTypeForTurn, null, __instance);
        }

        @SpireInsertPatch(locator = MultiDamageFinalLocator.class, localvars = {"tmp","i"})
        public static void multiFinal(AbstractCard __instance, float[] tmp, int i) {
            tmp[i] = atDamageFinalGive(tmp[i], __instance.damageTypeForTurn, null, __instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowersToBlock")
    public static class ApplyPowersToBlock {
        @SpireInsertPatch(locator = BlockLocator.class, localvars = {"tmp"})
        public static void blockInsert(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = onModifyBlock(tmp[0], __instance);
        }

        @SpireInsertPatch(locator = BlockFinalLocator.class, localvars = {"tmp"})
        public static void blockFinalInsert(AbstractCard __instance, @ByRef float[] tmp) {
            tmp[0] = onModifyBlockFinal(tmp[0], __instance);
        }
    }

    @SpirePatch2(clz = AbstractCreature.class, method = "addBlock")
    public static class OnGainBlock {
        @SpireInsertPatch(locator = CurrentBlockLocator.class, localvars = {"tmp"})
        public static void plz(AbstractCreature __instance, float tmp) {
            if (__instance.isPlayer) {
                onGainBlock(MathUtils.floor(tmp));
            }
        }
    }

    private static class CurrentBlockLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher m = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctBehavior, m);
        }
    }

    private static class MultiDamageFinalLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[3]};
        }
    }

    private static class DamageFinalLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[1]};
        }
    }

    private static class MultiDamageLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[2]};
        }
    }

    private static class DamageLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            int[] tmp = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{tmp[0]};
        }
    }

    private static class BlockFinalLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "floor");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class BlockLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    public interface InHandCard {
        default void onCardUsed(AbstractCard card) {}
        default void onCardDrawn(AbstractCard card) {}
        default void onCardExhausted(AbstractCard card) {}
        default void onDamaged(DamageInfo info) {}
        default void onStartTurnBlockLoss(int amountLost) {}
        default void onGainBlock(int block) {}
        default void onPowerApplied(AbstractPower power, AbstractCreature target, AbstractCreature source) {}
        default float atDamageGive(float dmg, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            return dmg;
        }
        default float atDamageFinalGive(float dmg, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
            return dmg;
        }
        default float onModifyBlock(float blk, AbstractCard card) {
            return blk;
        }
        default float onModifyBlockFinal(float blk, AbstractCard card) {
            return blk;
        }
    }
}
