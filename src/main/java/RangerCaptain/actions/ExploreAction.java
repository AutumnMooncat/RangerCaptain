package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.ui.DiscoveredCardManager;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ExploreAction extends AbstractGameAction {
    public static String ID = MainModfile.makeID(ExploreAction.class.getSimpleName());
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    public ExploreAction(int cardsToPickFrom) {
        this.amount = cardsToPickFrom;
    }

    @Override
    public void update() {
        if (amount == 0 || Wiz.adp().drawPile.isEmpty()) {
            this.isDone = true;
            return;
        }
        for (AbstractRelic r : Wiz.adp().relics) {
            if (r instanceof OnExploreModifier) {
                amount += ((OnExploreModifier) r).bonusOptions();
            }
        }
        for (AbstractPower p : Wiz.adp().powers) {
            if (p instanceof OnExploreModifier) {
                amount += ((OnExploreModifier) p).bonusOptions();
            }
        }
        if (amount > Wiz.adp().drawPile.size()) {
            amount = Wiz.adp().drawPile.size();
        }
        ArrayList<AbstractCard> cardsToPickFrom = new ArrayList<>();
        ArrayList<AbstractCard> validCards = new ArrayList<>(Wiz.adp().drawPile.group);
        for (int i = 0 ; i < amount ; i++) {
            AbstractCard c = Wiz.getRandomItem(validCards);
            if (c != null) {
                validCards.remove(c);
                cardsToPickFrom.add(c);
            }
        }
        int toSelect = 1;
        for (AbstractRelic r : Wiz.adp().relics) {
            if (r instanceof OnExploreModifier) {
                toSelect += ((OnExploreModifier) r).bonusPicks();
            }
        }
        for (AbstractPower p : Wiz.adp().powers) {
            if (p instanceof OnExploreModifier) {
                toSelect += ((OnExploreModifier) p).bonusPicks();
            }
        }
        addToTop(new BetterSelectCardsCenteredAction(cardsToPickFrom, toSelect, toSelect == 1 ? TEXT[0] : TEXT[1] + toSelect + TEXT[2], true, c -> true, l -> {
            for (AbstractCard c : l) {
                c.unhover();
                c.lighten(true);
                c.setAngle(0.0F);
                c.drawScale = 0.12F;
                c.targetDrawScale = 0.75F;
                c.current_x = CardGroup.DRAW_PILE_X;
                c.current_y = CardGroup.DRAW_PILE_Y;
                Wiz.adp().drawPile.removeCard(c);
                DiscoveredCardManager.addCard(c);
                CardCounterPatches.cardsExploredThisCombat++;
                CardCounterPatches.cardsExploredThisTurn++;
            }
        }));
        this.isDone = true;
    }

    public interface OnExploreModifier {
        int bonusOptions();
        int bonusPicks();
    }
}
