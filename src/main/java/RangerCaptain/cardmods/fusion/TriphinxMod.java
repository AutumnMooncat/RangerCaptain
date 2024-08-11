package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class TriphinxMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(TriphinxMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 2;


    public TriphinxMod() {
        super(ID);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
        if (card.hasTag(CustomTags.MAGIC_VULN)) {
            card.baseMagicNumber += AMOUNT;
            card.magicNumber += AMOUNT;
        }
        if (card.hasTag(CustomTags.SECOND_MAGIC_VULN) && card instanceof AbstractEasyCard) {
            ((AbstractEasyCard) card).baseSecondMagic += AMOUNT;
            ((AbstractEasyCard) card).secondMagic += AMOUNT;
        }
        ExtraEffectPatches.EffectFields.closeEncounter.set(card, true);
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VULN) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
        }
        if (!card.hasTag(CustomTags.CLOSE_ENCOUNTER)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[1]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_VULN) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN)) {
            Wiz.applyToEnemy((AbstractMonster) target, new VulnerablePower(target, AMOUNT, false));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TriphinxMod();
    }
}
