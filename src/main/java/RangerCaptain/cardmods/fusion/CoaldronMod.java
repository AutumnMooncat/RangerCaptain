package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CoaldronMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(CoaldronMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 5;
    public static final int AMOUNT2 = 1;

    public CoaldronMod() {
        super(ID);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_BURN) || card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            magic += AMOUNT;
        }
        if (card.hasTag(CustomTags.MAGIC_EXHAUST)) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_EXHAUST)) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
                card.target = AbstractCard.CardTarget.ENEMY;
            }
            if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
                card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
            }
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
                card.target = AbstractCard.CardTarget.ENEMY;
            }
            if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
                card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
            }
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT, AMOUNT2);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_BURN) && !card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            rawDescription = FormatHelper.insertBeforeText(rawDescription, String.format(CARD_TEXT[0], AMOUNT));
        }
        if (!card.hasTag(CustomTags.MAGIC_EXHAUST) && !card.hasTag(CustomTags.SECOND_MAGIC_EXHAUST)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[1], AMOUNT2));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_BURN) && !card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            Wiz.applyToEnemyTop((AbstractMonster) target, new BurnedPower(target, Wiz.adp(), AMOUNT));
        }
        if (!card.hasTag(CustomTags.MAGIC_EXHAUST) && !card.hasTag(CustomTags.SECOND_MAGIC_EXHAUST)) {
            Wiz.atb(new ExhaustAction(AMOUNT2, false, false));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CoaldronMod();
    }
}
