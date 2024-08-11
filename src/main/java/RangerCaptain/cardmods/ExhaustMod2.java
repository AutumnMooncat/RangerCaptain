package RangerCaptain.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static RangerCaptain.MainModfile.makeID;

public class ExhaustMod2 extends AbstractCardModifier {
    public static String ID = makeID(ExhaustMod.class.getSimpleName());
    public static String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ExhaustMod2() {
        this.priority = 10;
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + TEXT[0];
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return true;
    }

    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
    }

    public AbstractCardModifier makeCopy() {
        return new ExhaustMod2();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
