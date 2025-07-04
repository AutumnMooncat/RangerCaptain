package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SetCostMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(SetCostMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public int newCost;

    public SetCostMod(int newCost) {
        super(ID);
        this.newCost = newCost;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost = newCost;
            card.costForTurn = newCost;
        }
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], newCost);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SetCostMod(newCost);
    }
}
