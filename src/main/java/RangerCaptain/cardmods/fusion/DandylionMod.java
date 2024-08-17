package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.BetterSelectCardsCenteredAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Dandylion;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class DandylionMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(DandylionMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 4;
    public static final int AMOUNT2 = 1;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public DandylionMod() {
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
    public void onInitialApplication(AbstractCard card) {
        if (card instanceof Dandylion) {
            card.baseMagicNumber += AMOUNT2;
            card.magicNumber += AMOUNT2;
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.baseBlock == -1) {
            rawDescription = FormatHelper.insertBeforeText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        if (!(card instanceof Dandylion)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[1]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.baseBlock == -1) {
            Wiz.att(new GainBlockAction(Wiz.adp(), Wiz.adp(), val));
        }
        if (!(card instanceof Dandylion)) {
            addToBot(new BetterSelectCardsCenteredAction(Wiz.adp().discardPile.group, AMOUNT2, DiscardPileToTopOfDeckAction.TEXT[0], false, c -> true, cards -> {
                for (AbstractCard c : cards) {
                    Wiz.adp().discardPile.removeCard(c);
                    Wiz.adp().hand.moveToDeck(c, false);
                }
            }));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DandylionMod();
    }
}
