package RangerCaptain.actions;

import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;

public class DoForEachMiracleAction extends DoAction {
    public DoForEachMiracleAction(Runnable runnable) {
        super(() -> {
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (c instanceof Miracle) {
                    runnable.run();
                }
            }
        });
    }
}
