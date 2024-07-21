package RangerCaptain.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static RangerCaptain.MainModfile.makeID;

public class ExhaustMod extends AbstractCardModifier {
    public static String ID = makeID(ExhaustMod.class.getSimpleName());
    public static String[] TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[0];
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.exhaust;
    }

    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
    }

    public AbstractCardModifier makeCopy() {
        return new ExhaustMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
