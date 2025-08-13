package RangerCaptain.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static RangerCaptain.MainModfile.makeID;

public class ShaderTestMod extends AbstractCardModifier {
    public static String ID = makeID(ShaderTestMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return TEXT[0] + rawDescription;
    }

    public AbstractCardModifier makeCopy() {
        return new ShaderTestMod();
    }

    public String identifier(AbstractCard card) {
        return ID;
    }
}
