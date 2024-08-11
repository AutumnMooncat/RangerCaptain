package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AeroborosMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AeroborosMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AeroborosMod() {
        super(ID);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost > 0) {
            card.cost--;
            card.costForTurn--;
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AeroborosMod();
    }
}
