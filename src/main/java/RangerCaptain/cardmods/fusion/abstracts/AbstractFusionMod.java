package RangerCaptain.cardmods.fusion.abstracts;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public abstract class AbstractFusionMod extends AbstractCardModifier {
    public String identifier;

    public AbstractFusionMod(String identifier) {
        this.identifier = identifier;
    }

    public abstract String getModDescription(AbstractCard card);

    @Override
    public String identifier(AbstractCard card) {
        return identifier;
    }

    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        return magic;
    }

    public float modifyBaseThirdMagic(float magic, AbstractCard card) {
        return magic;
    }

    public void onUpgrade(AbstractCard card) {}
}
