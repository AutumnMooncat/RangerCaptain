package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

public class InstantExhaustSpecificCardAction extends ExhaustSpecificCardAction {
    public InstantExhaustSpecificCardAction(AbstractCard targetCard, CardGroup group) {
        super(targetCard, group);
    }

    @Override
    public void update() {
        super.update();
        this.isDone = true;
    }
}
