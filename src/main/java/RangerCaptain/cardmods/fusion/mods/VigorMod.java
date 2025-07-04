package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class VigorMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(VigorMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public VigorMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_VIGOR)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VIGOR) && !card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_VIGOR) && !card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            Wiz.applyToSelf(new VigorPower(Wiz.adp(), val));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new VigorMod(baseVal);
    }
}
