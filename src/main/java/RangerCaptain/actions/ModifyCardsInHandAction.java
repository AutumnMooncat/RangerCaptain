package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModifyCardsInHandAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("ModifyAction")).TEXT;
    private final Predicate<AbstractCard> filter;
    private final Consumer<List<AbstractCard>> callback;
    private final boolean anyAmount;

    public ModifyCardsInHandAction(int amount, Consumer<List<AbstractCard>> callback) {
        this(amount, false, c -> true, callback);
    }

    public ModifyCardsInHandAction(int amount, boolean anyAmount, Predicate<AbstractCard> filter, Consumer<List<AbstractCard>> callback) {
        this.amount = amount;
        this.anyAmount = anyAmount;
        this.filter = filter;
        this.callback = callback;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding() || amount <= 0) {
            this.isDone = true;
            return;
        }
        if (Wiz.adp().hand.isEmpty()) {
            this.isDone = true;
            return;
        }
        ArrayList<AbstractCard> validCards = Wiz.adp().hand.group.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
        if (validCards.isEmpty()) {
            this.isDone = true;
            return;
        }
        if (amount >= validCards.size() && !anyAmount) {
            callback.accept(validCards);
        } else {
            if (amount > validCards.size()) {
                amount = validCards.size();
            }
            Wiz.att(new BetterSelectCardsInHandAction(this.amount, TEXT[0], anyAmount, anyAmount, validCards::contains, callback));
        }
        this.isDone = true;
    }
}
