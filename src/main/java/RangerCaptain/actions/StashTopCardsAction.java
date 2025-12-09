package RangerCaptain.actions;

import RangerCaptain.ui.StashedCardManager;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StashTopCardsAction extends AbstractGameAction {
    private final Consumer<List<AbstractCard>> callback;
    private final List<AbstractCard> stashed;

    public StashTopCardsAction(int amount) {
        this(amount, l -> {});
    }

    public StashTopCardsAction(int amount, Consumer<List<AbstractCard>> callback) {
        this(amount, new ArrayList<>(), callback);
    }

    private StashTopCardsAction(int amount, List<AbstractCard> processed, Consumer<List<AbstractCard>> callback) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
        this.callback = callback;
        this.stashed = processed;
    }

    @Override
    public void update() {
        while (amount > 0 && !Wiz.adp().drawPile.isEmpty()) {
            AbstractCard top = Wiz.adp().drawPile.getTopCard();
            Wiz.adp().drawPile.removeCard(top);
            StashedCardManager.addCard(top);
            stashed.add(top);
            amount--;
        }

        if (amount > 0 && !Wiz.adp().discardPile.isEmpty()) {
            addToTop(new StashTopCardsAction(amount, stashed, callback));
            addToTop(new EmptyDeckShuffleAction());
            this.isDone = true;
            return;
        }

        callback.accept(stashed);
        isDone = true;
    }
}
