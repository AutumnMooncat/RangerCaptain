package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AddMindMeldMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AddMindMeldMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddMindMeldMod() {
        super(ID);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        ExtraEffectPatches.EffectFields.mindMeld.set(card, true);
    }

    @Override
    public String getModDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MIND_MELD)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
        }
        return rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddMindMeldMod();
    }
}
