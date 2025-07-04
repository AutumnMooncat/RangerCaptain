package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ResonanceMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(ResonanceMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ResonanceMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_RESONANCE) || card.hasTag(CustomTags.MAGIC_RESONANCE_AOE)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE) || card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE_AOE)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_RESONANCE_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE_AOE)) {
            makeTargeted(card);
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_RESONANCE_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE_AOE)) {
            makeTargeted(card);
        }
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_RESONANCE) && !card.hasTag(CustomTags.MAGIC_RESONANCE_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_RESONANCE) && !card.hasTag(CustomTags.MAGIC_RESONANCE_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE) && !card.hasTag(CustomTags.SECOND_MAGIC_RESONANCE_AOE)) {
            Wiz.applyToEnemy((AbstractMonster) target, new ResonancePower(target, val));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ResonanceMod(baseVal);
    }
}
