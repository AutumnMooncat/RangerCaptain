package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ChangeCostMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(ChangeCostMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public int change;

    public ChangeCostMod(int change) {
        super(ID);
        this.change = change;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost = Math.max(0, card.cost + change);
            card.costForTurn = Math.max(0, card.costForTurn + change);
        }
    }

    @Override
    public String getModDescription() {
        return change >= 0 ? String.format(DESCRIPTION_TEXT[0], change) : String.format(DESCRIPTION_TEXT[1], -change);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ChangeCostMod(change);
    }
}
