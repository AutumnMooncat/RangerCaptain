package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DamageMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(DamageMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;

    public DamageMod(int base, AbstractGameAction.AttackEffect effect) {
        super(ID, VarType.DAMAGE_DIRECT, base);
        this.effect = effect;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (damage != -1) {
            damage += getModifiedBase(card);;
        }
        return damage;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        makeAttack(card);
        if (!card.hasTag(CustomTags.AOE_DAMAGE)) {
            makeTargeted(card);
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        makeAttack(card);
        if (!card.hasTag(CustomTags.AOE_DAMAGE)) {
            makeTargeted(card);
        }
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.baseDamage == -1) {
            rawDescription = FormatHelper.insertAfterBlock(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.baseDamage == -1) {
            Wiz.attAfterBlock(new DamageAction(target, new DamageInfo(Wiz.adp(), val, DamageInfo.DamageType.NORMAL), effect));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DamageMod(baseVal, effect);
    }
}
