package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Braxsuit;
import RangerCaptain.powers.SuitUpPower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SuitUpMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(SuitUpMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public SuitUpMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Braxsuit) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public String getModDescription() {
        return baseVal == 1 ? DESCRIPTION_TEXT[0] : String.format(DESCRIPTION_TEXT[1], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(card instanceof Braxsuit)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!(card instanceof Braxsuit)) {
            Wiz.applyToSelf(new SuitUpPower(Wiz.adp(), val));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new SuitUpMod(baseVal);
    }
}
