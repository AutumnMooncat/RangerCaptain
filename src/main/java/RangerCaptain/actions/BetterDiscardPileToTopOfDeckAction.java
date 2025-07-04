package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class BetterDiscardPileToTopOfDeckAction extends BetterSelectCardsCenteredAction {
    public static final String ID = MainModfile.makeID(BetterDiscardPileToTopOfDeckAction.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public BetterDiscardPileToTopOfDeckAction(int amount) {
        super(Wiz.adp().discardPile.group, amount, amount == 0 ? DESCRIPTION_TEXT[1] : DESCRIPTION_TEXT[2] + amount + DESCRIPTION_TEXT[3], false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.adp().discardPile.removeCard(card);
                Wiz.adp().hand.moveToDeck(card, false);
            }
        });
    }
}
