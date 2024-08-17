package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.Dominoth;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class DominothMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(DominothMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 4;

    public DominothMod() {
        super(ID);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_VIGOR)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VIGOR) && !card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], AMOUNT));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_VIGOR) && !card.hasTag(CustomTags.SECOND_MAGIC_VIGOR)) {
            Wiz.applyToSelf(new VigorPower(Wiz.adp(), AMOUNT));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DominothMod();
    }
}
