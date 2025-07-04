package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.BetterDiscardPileToTopOfDeckAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Dandylion;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class DiscardToDrawMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(DiscardToDrawMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public DiscardToDrawMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Dandylion) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public String getModDescription() {
        return baseVal == 1 ? DESCRIPTION_TEXT[0] : String.format(DESCRIPTION_TEXT[1], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(card instanceof Dandylion)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!(card instanceof Dandylion)) {
            addToBot(new BetterDiscardPileToTopOfDeckAction(val));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DiscardToDrawMod(baseVal);
    }
}
