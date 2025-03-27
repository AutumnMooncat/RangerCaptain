package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.ui.DiscoveredCardManager;
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

public class DiscoverCopyOfCardsAction extends AbstractGameAction {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(DiscoverCopyOfCardsAction.class.getSimpleName())).TEXT;
    private final CardGroup group;
    private final int copies;
    private final boolean anyNumber;
    private final boolean canPickZero;
    private final Predicate<AbstractCard> filter;
    private final Consumer<List<AbstractCard>> callback;

    public DiscoverCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies) {
        this(sourceGroup, cardsToChoose, copies, false, false, c -> true, l -> {});
    }

    public DiscoverCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, boolean anyNumber, boolean canPickZero) {
        this(sourceGroup, cardsToChoose, copies, anyNumber, canPickZero, c -> true, l ->{});
    }

    public DiscoverCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, Predicate<AbstractCard> filter) {
        this(sourceGroup, cardsToChoose, copies, false, false, filter, l -> {});
    }

    public DiscoverCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this(sourceGroup, cardsToChoose, copies, false, false, filter, callback);
    }

    public DiscoverCopyOfCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, boolean anyNumber, boolean canPickZero, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
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
                        DiscoveredCardManager.addCard(c.makeStatEquivalentCopy());
                    }
                }
                callback.accept(validCards);
            } else {
                if (group == Wiz.adp().hand) {
                    addToTop(new BetterSelectCardsInHandAction(amount, TEXT[0], anyNumber, canPickZero, filter, cards -> {
                        for (AbstractCard c : cards) {
                            for (int i = 0 ; i < copies ; i++) {
                                DiscoveredCardManager.addCard(c.makeStatEquivalentCopy());
                            }
                        }
                        callback.accept(cards);
                    }));
                } else {
                    addToTop(new BetterSelectCardsCenteredAction(validCards, amount, TEXT[0], anyNumber, c -> true, cards -> {
                        for (AbstractCard c : cards) {
                            for (int i = 0 ; i < copies ; i++) {
                                DiscoveredCardManager.addCard(c.makeStatEquivalentCopy());
                            }
                        }
                        callback.accept(cards);
                    }));
                }
            }
        }
        this.isDone = true;
    }
}
