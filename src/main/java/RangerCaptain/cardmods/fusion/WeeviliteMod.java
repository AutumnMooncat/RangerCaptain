package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class WeeviliteMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(WeeviliteMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 6;
    public static final int AMOUNT2 = 1;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public WeeviliteMod() {
        super(ID, VarType.BLOCK, AMOUNT);
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        if (block != -1) {
            block += AMOUNT;
        }
        return block;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_DRAW_NEXT_TURN)) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_DRAW_NEXT_TURN)) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.baseBlock == -1) {
            rawDescription = FormatHelper.insertBeforeText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        if (!card.hasTag(CustomTags.MAGIC_DRAW_NEXT_TURN) && !card.hasTag(CustomTags.SECOND_MAGIC_DRAW_NEXT_TURN)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[1]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.baseBlock == -1) {
            Wiz.att(new GainBlockAction(Wiz.adp(), Wiz.adp(), val));
        }
        if (!card.hasTag(CustomTags.MAGIC_DRAW_NEXT_TURN) && !card.hasTag(CustomTags.SECOND_MAGIC_DRAW_NEXT_TURN)) {
            Wiz.applyToSelf(new DrawCardNextTurnPower(Wiz.adp(), AMOUNT2));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new WeeviliteMod();
    }
}
