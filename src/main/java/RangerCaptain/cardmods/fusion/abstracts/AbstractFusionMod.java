package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractFusionMod extends AbstractCardModifier {
    public String identifier;
    public String modDescription;
    public String cardText;

    public AbstractFusionMod(String identifier, String modDescription, String cardText) {
        this.identifier = identifier;
        this.modDescription = modDescription;
        this.cardText = cardText;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (priority < 0) {
            return FormatHelper.insertBeforeText(rawDescription, cardText);
        } else {
            return FormatHelper.insertAfterText(rawDescription, cardText);
        }
    }

    @Override
    public String identifier(AbstractCard card) {
        return identifier;
    }

}
