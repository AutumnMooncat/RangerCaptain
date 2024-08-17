package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Squirey;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ManispearMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(ManispearMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 5;
    public static final int AMOUNT2 = 3;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public ManispearMod() {
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
        if (card instanceof Squirey) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT, AMOUNT2);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.baseBlock == -1) {
            rawDescription = FormatHelper.insertBeforeText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        if (!(card instanceof Squirey)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[1], AMOUNT2, AMOUNT2));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.baseBlock == -1) {
            Wiz.att(new GainBlockAction(Wiz.adp(), Wiz.adp(), val));
        }
        if (!(card instanceof Squirey)) {
            Wiz.applyToSelf(new StrengthPower(Wiz.adp(), AMOUNT2));
            Wiz.applyToSelf(new LoseStrengthPower(Wiz.adp(), AMOUNT2));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ManispearMod();
    }
}
