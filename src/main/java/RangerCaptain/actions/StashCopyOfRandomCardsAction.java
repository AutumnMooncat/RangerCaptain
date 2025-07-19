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

public class StashCopyOfRandomCardsAction extends AbstractGameAction {
    private final CardGroup group;
    private final Consumer<List<AbstractCard>> callback;
    private final Predicate<AbstractCard> filter;
    private final int copies;

    public StashCopyOfRandomCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies) {
        this(sourceGroup, cardsToChoose, copies, c -> true, l -> {});
    }

    public StashCopyOfRandomCardsAction(CardGroup sourceGroup, int cardsToChoose, int copies, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.group = sourceGroup;
        this.amount = cardsToChoose;
        this.copies = copies;
        this.filter = filter;
        this.callback = callback;
    }

    @Override
    public void update() {
        List<AbstractCard> valid = group.group.stream().filter(filter).collect(Collectors.toList());
        List<AbstractCard> stashed = new ArrayList<>();
        while (amount > 0 && !valid.isEmpty()) {
            AbstractCard card = valid.remove(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
            for (int i = 0; i < copies; i++) {
                AbstractCard copy = card.makeStatEquivalentCopy();
                StashedCardManager.addCard(copy);
                stashed.add(copy);
            }
            amount--;
        }
        callback.accept(stashed);
        this.isDone = true;
    }
}
