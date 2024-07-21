package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.interfaces.OnGuidedCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.powers.interfaces.OnGuidePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class GuideAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("GuideAction")).TEXT;
    private static final float DUR = Settings.ACTION_DUR_FASTER;
    private final CardGroup cardGroup;
    private final Predicate<AbstractCard> filter;
    private final AbstractGameAction followUpAction;
    private final boolean anyNumber;
    public static final ArrayList<AbstractCard> guidedCards = new ArrayList<>();

    public GuideAction(CardGroup cardGroup) {
        this(cardGroup, 1, c -> true, null);
    }

    public GuideAction(CardGroup cardGroup, int amount) {
        this(cardGroup, amount, c -> true, null);
    }

    public GuideAction(CardGroup cardGroup, int amount, Predicate<AbstractCard> filter) {
        this(cardGroup, amount, filter, null);
    }

    public GuideAction(CardGroup cardGroup, int amount, Predicate<AbstractCard> filter, AbstractGameAction followUpAction) {
        this(cardGroup, amount, false, filter, followUpAction);
    }

    public GuideAction(CardGroup cardGroup, int amount, boolean anyNumber, Predicate<AbstractCard> filter, AbstractGameAction followUpAction) {
        this.cardGroup = cardGroup;
        this.amount = amount;
        this.filter = filter;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = DUR;
        this.followUpAction = followUpAction;
        this.anyNumber = anyNumber;
    }

    @Override
    public void update() {
        guidedCards.clear();
        if (AbstractDungeon.getCurrRoom().isBattleEnding() || amount == 0) {
            this.isDone = true;
        }
        ArrayList<AbstractCard> validCards = new ArrayList<>();
        for (AbstractCard c : cardGroup.group) {
            if (filter.test(c)) {
                validCards.add(c);
            }
        }
        if (validCards.isEmpty()) {
            this.isDone = true;
            return;
        }

        if (validCards.size() == 1 && !anyNumber) {
            //Collections.reverse(validCards);
            for (AbstractCard card : validCards) {
                cardGroup.removeCard(card);
                cardGroup.moveToDeck(card, false);
                guidedCards.add(card);
                triggerEffects(card);
            }
            if (this.followUpAction != null) {
                this.addToTop(this.followUpAction);
            }
            CardCounterPatches.cardsGuidedThisTurn += guidedCards.size();
            CardCounterPatches.cardsGuidedThisCombat += guidedCards.size();
        } else {
            if (amount > validCards.size()) {
                amount = validCards.size();
            }
            if (cardGroup == Wiz.adp().hand) {
                Wiz.att(new BetterSelectCardsInHandAction(this.amount, TEXT[0], anyNumber, anyNumber, validCards::contains, cards -> {
                    Collections.reverse(cards);
                    for (AbstractCard c : cards) {
                        //cardGroup.removeCard(c);
                        cardGroup.moveToDeck(c, false);
                        guidedCards.add(c);
                        triggerEffects(c);
                    }
                    if (this.followUpAction != null) {
                        this.addToTop(this.followUpAction);
                    }
                    CardCounterPatches.cardsGuidedThisTurn += guidedCards.size();
                    CardCounterPatches.cardsGuidedThisCombat += guidedCards.size();
                    cards.clear(); // Remove from selection, so they don't get added back to hand
                }));
            } else {
                Wiz.att(new BetterSelectCardsCenteredAction(validCards, this.amount, amount == 1 ? TEXT[1] : TEXT[2] + amount + TEXT[3], anyNumber, true, cards -> {
                    Collections.reverse(cards);
                    for (AbstractCard c : cards) {
                        cardGroup.removeCard(c);
                        cardGroup.moveToDeck(c, false);
                        guidedCards.add(c);
                        triggerEffects(c);
                    }
                    if (this.followUpAction != null) {
                        this.addToTop(this.followUpAction);
                    }
                    CardCounterPatches.cardsGuidedThisTurn += guidedCards.size();
                    CardCounterPatches.cardsGuidedThisCombat += guidedCards.size();
                }));
            }

        }
        this.isDone = true;
    }

    private static void triggerEffects(AbstractCard card) {
        if (card instanceof OnGuidedCard) {
            ((OnGuidedCard) card).onGuided();
        }
        for (AbstractPower p : Wiz.adp().powers) {
            if (p instanceof OnGuidePower) {
                ((OnGuidePower) p).onGuide(card);
            }
        }
    }
}
