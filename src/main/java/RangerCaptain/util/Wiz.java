package RangerCaptain.util;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.actions.TimedVFXAction;
import RangerCaptain.cardmods.CarrotMod;
import RangerCaptain.cardmods.FusionFormMod;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.ReskinComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.powers.LosePowerPower;
import RangerCaptain.powers.NextTurnPowerPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Wiz {
    //The wonderful Wizard of Oz allows access to most easy compilations of data, or functions.

    public static AbstractPlayer adp() {
        return AbstractDungeon.player;
    }

    public static void forAllCardsInList(Consumer<AbstractCard> consumer, ArrayList<AbstractCard> cardsList) {
        for (AbstractCard c : cardsList) {
            consumer.accept(c);
        }
    }

    public static ArrayList<AbstractCard> getAllCardsInCardGroups(boolean includeHand, boolean includeExhaust) {
        ArrayList<AbstractCard> masterCardsList = new ArrayList<>();
        masterCardsList.addAll(AbstractDungeon.player.drawPile.group);
        masterCardsList.addAll(AbstractDungeon.player.discardPile.group);
        if (includeHand) {
            masterCardsList.addAll(AbstractDungeon.player.hand.group);
        }
        if (includeExhaust) {
            masterCardsList.addAll(AbstractDungeon.player.exhaustPile.group);
        }
        return masterCardsList;
    }

    public static void forAllMonstersLiving(Consumer<AbstractMonster> consumer) {
        for (AbstractMonster m : getEnemies()) {
            consumer.accept(m);
        }
    }

    public static ArrayList<AbstractMonster> getEnemies() {
        ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getMonsters().monsters);
        monsters.removeIf(m -> m.isDead || m.isDying);
        return monsters;
    }

    public static ArrayList<AbstractCard> getCardsMatchingPredicate(Predicate<AbstractCard> pred) {
        return getCardsMatchingPredicate(pred, false);
    }

    public static ArrayList<AbstractCard> getCardsMatchingPredicate(Predicate<AbstractCard> pred, boolean allcards) {
        if (allcards) {
            ArrayList<AbstractCard> cardsList = new ArrayList<>();
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (pred.test(c)) cardsList.add(c.makeStatEquivalentCopy());
            }
            return cardsList;
        } else {
            ArrayList<AbstractCard> cardsList = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.srcCommonCardPool.group) {
                if (pred.test(c)) cardsList.add(c.makeStatEquivalentCopy());
            }
            for (AbstractCard c : AbstractDungeon.srcUncommonCardPool.group) {
                if (pred.test(c)) cardsList.add(c.makeStatEquivalentCopy());
            }
            for (AbstractCard c : AbstractDungeon.srcRareCardPool.group) {
                if (pred.test(c)) cardsList.add(c.makeStatEquivalentCopy());
            }
            return cardsList;
        }
    }

    public static AbstractCard returnTrulyRandomPrediCardInCombat(Predicate<AbstractCard> pred, boolean allCards) {
        return getRandomItem(getCardsMatchingPredicate(pred, allCards));
    }


    public static AbstractCard returnTrulyRandomPrediCardInCombat(Predicate<AbstractCard> pred) {
        return returnTrulyRandomPrediCardInCombat(pred, false);
    }

    public static <T> T getRandomItem(ArrayList<T> list, Random rng) {
        return list.isEmpty() ? null : list.get(rng.random(list.size() - 1));
    }

    public static <T> T getRandomItem(ArrayList<T> list) {
        return getRandomItem(list, AbstractDungeon.cardRandomRng);
    }

    private static boolean actuallyHovered(Hitbox hb) {
        return InputHelper.mX > hb.x && InputHelper.mX < hb.x + hb.width && InputHelper.mY > hb.y && InputHelper.mY < hb.y + hb.height;
    }

    public static boolean isInCombat() {
        return CardCrawlGame.isInARun() && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public static boolean isCombatCard(AbstractCard card) {
        return !Wiz.adp().masterDeck.contains(card) && !CardCrawlGame.cardPopup.isOpen;
    }

    public static void atb(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void att(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static void attAfterBlock(AbstractGameAction action) {
        if (!AbstractDungeon.actionManager.actions.isEmpty()) {
            AbstractGameAction topAction = AbstractDungeon.actionManager.actions.get(0);
            if (topAction instanceof GainBlockAction) {
                AbstractDungeon.actionManager.actions.remove(topAction);
                AbstractDungeon.actionManager.addToTop(action);
                AbstractDungeon.actionManager.addToTop(topAction);
            }
        } else {
            AbstractDungeon.actionManager.addToTop(action);
        }

    }

    public static void sequenceActions(boolean toTop, AbstractGameAction... actions) {
        if (toTop) {
            for (int i = actions.length - 1; i >= 0; i--) {
                att(actions[i]);
            }
        } else {
            for (AbstractGameAction action : actions) {
                atb(action);
            }
        }
    }

    public static void vfx(AbstractGameEffect gameEffect) {
        atb(new VFXAction(gameEffect));
    }

    public static void vfx(AbstractGameEffect gameEffect, float duration) {
        atb(new VFXAction(gameEffect, duration));
    }

    public static void tfx(AbstractGameEffect gameEffect) {
        atb(new TimedVFXAction(gameEffect));
    }

    public static void makeInHand(AbstractCard c, int i) {
        atb(new MakeTempCardInHandAction(c, i));
    }

    public static void makeInHand(AbstractCard c) {
        makeInHand(c, 1);
    }

    public static void shuffleIn(AbstractCard c, int i) {
        atb(new MakeTempCardInDrawPileAction(c, i, true, true));
    }

    public static void shuffleIn(AbstractCard c) {
        shuffleIn(c, 1);
    }

    public static void topDeck(AbstractCard c, int i) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, i, false, true));
    }

    public static void topDeck(AbstractCard c) {
        topDeck(c, 1);
    }

    public static void applyToEnemy(AbstractMonster m, AbstractPower po) {
        atb(new ApplyPowerAction(m, AbstractDungeon.player, po, po.amount));
    }

    public static void applyToEnemyTop(AbstractMonster m, AbstractPower po) {
        att(new ApplyPowerAction(m, AbstractDungeon.player, po, po.amount));
    }

    public static void applyToSelf(AbstractPower po) {
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, po, po.amount));
    }

    public static void applyToSelfTop(AbstractPower po) {
        att(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, po, po.amount));
    }

    public static void applyToSelfTemp(AbstractPower po) {
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, po, po.amount));
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LosePowerPower(AbstractDungeon.player, po, po.amount)));
    }

    public static void applyToSelfNextTurn(AbstractPower po) {
        atb(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnPowerPower(AbstractDungeon.player, po)));
    }

    public static boolean isUnaware(AbstractMonster m) {
        return m != null && !m.isDeadOrEscaped() && m.getIntentDmg() < 5;
    }

    public static boolean anyMonsterUnaware() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (isUnaware(m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAttacking(AbstractMonster m) {
        return m != null && !m.isDeadOrEscaped() && m.getIntentBaseDmg() >= 0;
    }

    public static boolean anyMonsterAttacking() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (isAttacking(m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean anyMonsterNotAttacking() {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!isAttacking(m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isScavenged(AbstractCard c) {
        return !CardCounterPatches.initialHand.contains(c) || CardCounterPatches.cardsDrawnThisTurn.lastIndexOf(c) >= CardCounterPatches.initialHand.size();
    }

    public static List<AbstractCard> cardsPlayedThisCombat() {
        if (isInCombat()) {
            return AbstractDungeon.actionManager.cardsPlayedThisCombat;
        }
        return Collections.emptyList();
    }

    public static List<AbstractCard> cardsPlayedThisTurn() {
        if (isInCombat()) {
            return AbstractDungeon.actionManager.cardsPlayedThisTurn;
        }
        return Collections.emptyList();
    }

    public static AbstractCard lastCardPlayed() {
        if (isInCombat() && !cardsPlayedThisCombat().isEmpty()) {
            return cardsPlayedThisCombat().get(cardsPlayedThisCombat().size()-1);
        }
        return null;
    }

    public static AbstractCard lastCardPlayedThisTurn() {
        if (isInCombat() && !cardsPlayedThisTurn().isEmpty()) {
            return cardsPlayedThisTurn().get(cardsPlayedThisTurn().size()-1);
        }
        return null;
    }

    public static AbstractCard secondLastCardPlayed() {
        if (isInCombat() && cardsPlayedThisCombat().size() >= 2) {
            return cardsPlayedThisCombat().get(cardsPlayedThisCombat().size()-2);
        }
        return null;
    }

    public static void forAdjacentCards(AbstractCard thisCard, Consumer<AbstractCard> consumer) {
        int lastIndex = Wiz.adp().hand.group.indexOf(thisCard);
        if (lastIndex != -1) {
            if (lastIndex > 0) {
                consumer.accept(Wiz.adp().hand.group.get(lastIndex - 1));
            }
            if (lastIndex < Wiz.adp().hand.group.size()-1) {
                consumer.accept(Wiz.adp().hand.group.get(lastIndex + 1));
            }
        }
    }

    public static ArrayList<AbstractCard> getAdjacentCards(AbstractCard thisCard) {
        ArrayList<AbstractCard> ret = new ArrayList<>();
        forAdjacentCards(thisCard, ret::add);
        return ret;
    }

    public static void forAdjacentMonsters(AbstractCreature entity, Consumer<AbstractMonster> consumer) {
        ArrayList<Hitbox> hitboxes = new ArrayList<>();
        HashMap<Hitbox, AbstractMonster> hitMap = new HashMap<>();
        hitboxes.add(entity.hb);
        for (AbstractMonster m : Wiz.getEnemies()) {
            hitMap.put(m.hb, m);
            if (!hitboxes.contains(m.hb)) {
                hitboxes.add(m.hb);
            }
        }
        hitboxes.sort((h1, h2) -> Float.compare(h1.cX, h2.cX));
        int index = hitboxes.indexOf(entity.hb);
        if (index > 0) {
            consumer.accept(hitMap.get(hitboxes.get(index - 1)));
        }
        if (index < hitboxes.size() - 1) {
            consumer.accept(hitMap.get(hitboxes.get(index + 1)));
        }
    }

    public static ArrayList<AbstractMonster> getAdjacentMonsters(AbstractCreature entity) {
        ArrayList<AbstractMonster> ret = new ArrayList<>();
        forAdjacentMonsters(entity, ret::add);
        return ret;
    }

    public static boolean isInRabbitRun() {
        return CardCrawlGame.isInARun() && AbstractDungeon.player instanceof TheRangerCaptain;
    }

    public static int carrotCount(AbstractCard card) {
        int amount = 0;
        if (CardModifierManager.hasModifier(card, CarrotMod.ID)) {
            amount = ((CarrotMod) CardModifierManager.getModifiers(card, CarrotMod.ID).get(0)).amount;
        }
        return amount;
    }

    public static boolean canBeFused(AbstractCard card) {
        return card instanceof AbstractEasyCard && ((AbstractEasyCard) card).getMonsterData() != null && !CardModifierManager.hasModifier(card, FusionFormMod.ID);
    }

    public static FusedCard fuse(AbstractCard base, AbstractCard donor) {
        if (base instanceof AbstractEasyCard && donor instanceof AbstractEasyCard && ((AbstractEasyCard) base).getMonsterData() != null && ((AbstractEasyCard) donor).getMonsterData() != null) {
            List<AbstractComponent> components = FusionCardEffectData.getCombinedComponents(((AbstractEasyCard) base).getMonsterData(), ((AbstractEasyCard) donor).getMonsterData());
            AbstractCard.CardRarity rarity = AbstractCard.CardRarity.COMMON;
            if (base.rarity == AbstractCard.CardRarity.RARE || donor.rarity == AbstractCard.CardRarity.RARE) {
                rarity = AbstractCard.CardRarity.RARE;
            } else if (base.rarity == AbstractCard.CardRarity.UNCOMMON || donor.rarity == AbstractCard.CardRarity.UNCOMMON) {
                rarity = AbstractCard.CardRarity.UNCOMMON;
            }
            CardArtRoller.ReskinInfo ref = ((AbstractEasyCard) base).reskinInfo("");
            components.add(new ReskinComponent(rarity, ref.anchor1, ref.anchor2, ref.target1, ref.target2, ref.flipX));
            FusedCard fusion = new FusedCard(components);
            CardModifierManager.addModifier(fusion, new FusionFormMod(((AbstractEasyCard) base).getMonsterData(), ((AbstractEasyCard) donor).getMonsterData()));
            return fusion;
        }
        return new FusedCard();
    }
}
