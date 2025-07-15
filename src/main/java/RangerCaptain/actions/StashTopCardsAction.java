package RangerCaptain.actions;

import RangerCaptain.ui.StashedCardManager;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StashTopCardsAction extends AbstractGameAction {
    private final Consumer<List<AbstractCard>> callback;
    public StashTopCardsAction(int amount) {
        this(amount, l -> {});
    }

    public StashTopCardsAction(int amount, Consumer<List<AbstractCard>> callback) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.amount = amount;
        this.callback = callback;
    }

    @Override
    public void update() {
        List<AbstractCard> stashed = new ArrayList<>();
        while (amount > 0 && !Wiz.adp().drawPile.isEmpty()) {
            AbstractCard top = Wiz.adp().drawPile.getTopCard();
            Wiz.adp().drawPile.removeCard(top);
            StashedCardManager.addCard(top);
            stashed.add(top);
            amount--;
        }
        callback.accept(stashed);
        this.isDone = true;
    }
}
