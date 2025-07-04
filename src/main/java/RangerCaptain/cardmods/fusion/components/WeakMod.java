package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class WeakMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(WeakMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public WeakMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_WEAK) || card.hasTag(CustomTags.MAGIC_WEAK_AOE)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.SECOND_MAGIC_WEAK) || card.hasTag(CustomTags.SECOND_MAGIC_WEAK_AOE)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_WEAK_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK_AOE)) {
            makeTargeted(card);
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_WEAK_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK_AOE)) {
            makeTargeted(card);
        }
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_WEAK) && !card.hasTag(CustomTags.MAGIC_WEAK_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_WEAK) && !card.hasTag(CustomTags.MAGIC_WEAK_AOE) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK) && !card.hasTag(CustomTags.SECOND_MAGIC_WEAK_AOE)) {
            Wiz.applyToEnemy((AbstractMonster) target, new WeakPower(target, val, false));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new WeakMod(baseVal);
    }
}
