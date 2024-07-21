package RangerCaptain.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static RangerCaptain.MainModfile.makeID;

public class EtherealMod extends AbstractCardModifier {
    public static String ID = makeID(EtherealMod.class.getSimpleName());
    public static String[] TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }

    public boolean shouldApply(AbstractCard card) {
        return !card.isEthereal;
    }

    public void onInitialApplication(AbstractCard card) {
        card.isEthereal = true;
    }

    public AbstractCardModifier makeCopy() {
        return new EtherealMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
