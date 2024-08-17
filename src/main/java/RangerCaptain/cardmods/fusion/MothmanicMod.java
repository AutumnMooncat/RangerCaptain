package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MothmanicMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(MothmanicMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 6;

    public MothmanicMod() {
        super(ID);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_CONDUCTIVE)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_CONDUCTIVE)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_CONDUCTIVE) && !card.hasTag(CustomTags.SECOND_MAGIC_CONDUCTIVE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], AMOUNT));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_CONDUCTIVE) && !card.hasTag(CustomTags.SECOND_MAGIC_CONDUCTIVE)) {
            Wiz.applyToEnemy((AbstractMonster) target, new ConductivePower(target, Wiz.adp(), AMOUNT));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MothmanicMod();
    }
}
