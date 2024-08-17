package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
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
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_VULN) || card.hasTag(CustomTags.MAGIC_VULN_AOE)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_VULN_AOE)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VULN_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN_AOE)) {
            if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
                card.target = AbstractCard.CardTarget.ENEMY;
            }
            if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
                card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
            }
        }
        ExtraEffectPatches.EffectFields.closeEncounter.set(card, true);
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VULN_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN_AOE)) {
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
        return String.format(DESCRIPTION_TEXT[0], AMOUNT);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_VULN) && !card.hasTag(CustomTags.MAGIC_VULN_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], AMOUNT));
        }
        if (!card.hasTag(CustomTags.CLOSE_ENCOUNTER)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[1]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_VULN) && !card.hasTag(CustomTags.MAGIC_VULN_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN_AOE)) {
            Wiz.applyToEnemy((AbstractMonster) target, new VulnerablePower(target, AMOUNT, false));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new TriphinxMod();
    }
}
