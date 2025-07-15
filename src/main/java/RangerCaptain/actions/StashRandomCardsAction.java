package RangerCaptain.actions;

import RangerCaptain.ui.StashedCardManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StashRandomCardsAction extends AbstractGameAction {
    private final CardGroup group;
    private final Consumer<List<AbstractCard>> callback;
    private final Predicate<AbstractCard> filter;

    public StashRandomCardsAction(CardGroup sourceGroup, int amount) {
        this(sourceGroup, amount, c -> true, l -> {});
    }

    public StashRandomCardsAction(CardGroup sourceGroup, int amount, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.group = sourceGroup;
        this.amount = amount;
        this.filter = filter;
        this.callback = callback;
    }


    @Override
    public void update() {
        List<AbstractCard> valid = group.group.stream().filter(filter).collect(Collectors.toList());
        List<AbstractCard> stashed = new ArrayList<>();
        while (amount > 0 && !valid.isEmpty()) {
            AbstractCard card = valid.get(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
            group.removeCard(card);
            StashedCardManager.addCard(card);
            stashed.add(card);
            amount--;
        }
        callback.accept(stashed);
        this.isDone = true;
    }
}
