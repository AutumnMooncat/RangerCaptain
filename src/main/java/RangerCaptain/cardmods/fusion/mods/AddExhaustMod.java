package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AddExhaustMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AddExhaustMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddExhaustMod() {
        super(ID);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !card.exhaust;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.exhaust = true;
    }

    @Override
    public String getModDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + CARD_TEXT[0];
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddExhaustMod();
    }
}
