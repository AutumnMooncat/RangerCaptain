package RangerCaptain.cardmods.fusion;

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
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class MasquerattleMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(MasquerattleMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 4;
    public static final int AMOUNT2 = 3;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public MasquerattleMod() {
        super(ID, VarType.DAMAGE_DIRECT, AMOUNT);
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (damage != -1) {
            damage += AMOUNT;
        }
        return damage;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
        if (card.hasTag(CustomTags.MAGIC_POISON) || card.hasTag(CustomTags.MAGIC_POISON_AOE)) {
            card.baseMagicNumber += AMOUNT2;
            card.magicNumber += AMOUNT2;
        }
        if (card.type != AbstractCard.CardType.ATTACK && card instanceof AbstractEasyCard) {
            if (card.type == AbstractCard.CardType.POWER) {
                CardModifierManager.addModifier(card, new PurgeMod());
            }
            card.type = AbstractCard.CardType.ATTACK;
            ((AbstractEasyCard) card).rollerKey += "Attack";
            CardArtRoller.computeCard((AbstractEasyCard) card);
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.baseDamage == -1) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        if (!card.hasTag(CustomTags.MAGIC_POISON) && !card.hasTag(CustomTags.MAGIC_POISON_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[1]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.baseDamage == -1) {
            Wiz.atb(new DamageAction(target, new DamageInfo(Wiz.adp(), val, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        if (!card.hasTag(CustomTags.MAGIC_VULN) && !card.hasTag(CustomTags.SECOND_MAGIC_VULN)) {
            Wiz.applyToEnemy((AbstractMonster) target, new PoisonPower(target, Wiz.adp(), AMOUNT2));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MasquerattleMod();
    }
}
