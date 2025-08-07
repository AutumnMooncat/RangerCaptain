package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.ui.StashedCardManager;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StashCopyOfCardsAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(StashCopyOfCardsAction.class.getSimpleName())).TEXT;
    private final CardGroup group;
    private final int copies;
    private final boolean anyNumber;
    private final boolean canPickZero;
    private final Predicate<AbstractCard> filter;
    private final Consumer<List<AbstractCard>> callback;

    public StashCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies) {
        this(sourceGroup, cardsToChoose, copies, false, false, c -> true, l -> {});
    }

    public StashCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, boolean anyNumber, boolean canPickZero) {
        this(sourceGroup, cardsToChoose, copies, anyNumber, canPickZero, c -> true, l ->{});
    }

    public StashCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, Predicate<AbstractCard> filter) {
        this(sourceGroup, cardsToChoose, copies, false, false, filter, l -> {});
    }

    public StashCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this(sourceGroup, cardsToChoose, copies, false, false, filter, callback);
    }

    public StashCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.source = AbstractDungeon.player;
        this.group = sourceGroup;
        this.amount = cardsToChoose;
        this.copies = copies;
        this.anyNumber = anyNumber;
        this.canPickZero = canPickZero;
        this.filter = filter;
        this.callback = callback;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
    }
    @Override
    public void update() {
        if (duration == startDuration) {
            ArrayList<AbstractCard> validCards = new ArrayList<>();
            List<AbstractCard> stashed = new ArrayList<>();
            for (AbstractCard card : group.group) {
                if (filter.test(card)) {
                    validCards.add(card);
                }
            }
            if (validCards.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (validCards.size() <= amount && !anyNumber && !canPickZero) {
                for (AbstractCard c : validCards) {
                    for (int i = 0 ; i < copies ; i++) {
                        AbstractCard copy = c.makeStatEquivalentCopy();
                        StashedCardManager.addCard(copy);
                        stashed.add(copy);
                    }
                }
                callback.accept(stashed);
            } else {
                if (group == Wiz.adp().hand) {
                    addToTop(new BetterSelectCardsInHandAction(amount, TEXT[0], anyNumber, canPickZero, filter, cards -> {
                        for (AbstractCard c : cards) {
                            for (int i = 0 ; i < copies ; i++) {
                                AbstractCard copy = c.makeStatEquivalentCopy();
                                StashedCardManager.addCard(copy);
                                stashed.add(copy);
                            }
                        }
                        callback.accept(stashed);
                    }));
                } else {
                    String text = TEXT[1];
                    if (amount > 1) {
                        if (anyNumber) {
                            text = TEXT[5] + amount + TEXT[6];
                        } else {
                            text = TEXT[2] + amount + TEXT[3];
                        }
                    } else if (anyNumber) {
                        text = TEXT[4];
                    }
                    addToTop(new BetterSelectCardsCenteredAction(validCards, amount, text, anyNumber, c -> true, cards -> {
                        for (AbstractCard c : cards) {
                            for (int i = 0 ; i < copies ; i++) {
                                AbstractCard copy = c.makeStatEquivalentCopy();
                                StashedCardManager.addCard(copy);
                                stashed.add(copy);
                            }
                        }
                        callback.accept(stashed);
                    }));
                }
            }
        }
        this.isDone = true;
    }
}
