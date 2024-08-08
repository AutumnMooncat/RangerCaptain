package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.FusionMod;
import RangerCaptain.util.Wiz;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

public class FusionAction extends BetterSelectCardsInHandAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(FusionAction.class.getSimpleName()));

    public FusionAction() {
        super(2, uiStrings.TEXT[0], false, false, Wiz::canBeFused, cards -> {
            if (cards.size() == 2) {
                CardModifierManager.addModifier(cards.get(0), new FusionMod(cards.get(1)));
                if (Wiz.adp().hand.contains(cards.get(1))) {
                    Wiz.adp().hand.removeCard(cards.get(1));
                } else {
                    AbstractDungeon.handCardSelectScreen.selectedCards.group.remove(1);
                }
            }
        });
    }
}
