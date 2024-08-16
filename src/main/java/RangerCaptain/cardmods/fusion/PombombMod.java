package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Pombomb;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class PombombMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(PombombMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 4;
    public static final int AMOUNT2 = 2;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public PombombMod() {
        super(ID, VarType.DAMAGE_ALL, AMOUNT);
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (card.hasTag(CustomTags.AOE_DAMAGE)) {
            damage += AMOUNT;
        }
        return damage;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            magic += AMOUNT2;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
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
        if (!card.hasTag(CustomTags.AOE_DAMAGE) && !card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            rawDescription = FormatHelper.insertAfterBlock(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        } else if (!card.hasTag(CustomTags.AOE_DAMAGE)) {
            rawDescription = FormatHelper.insertAfterBlock(rawDescription, String.format(CARD_TEXT[1], descriptionKey()));
        } else if (!card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            rawDescription = FormatHelper.insertAfterBlock(rawDescription, CARD_TEXT[2]);
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_BURN_AOE)) {
            Wiz.forAllMonstersLiving(mon -> Wiz.attAfterBlock(new ApplyPowerAction(mon, Wiz.adp(), new BurnedPower(mon, Wiz.adp(), AMOUNT2))));
        }
        if (!card.hasTag(CustomTags.AOE_DAMAGE)) {
            Wiz.attAfterBlock(new DamageAllEnemiesAction(Wiz.adp(), multiVal, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new PombombMod();
    }
}
