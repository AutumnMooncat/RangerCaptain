package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class KhufoMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(KhufoMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public KhufoMod() {
        super(ID);
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        if (block > 1) {
            block /= 2;
        }
        return block;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (damage > 1) {
            damage /= 2;
        }
        return damage;
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (magic > 1) {
            magic /= 2;
        }
        return magic;
    }

    @Override
    public float modifyBaseSecondMagic(float magic, AbstractCard card) {
        if (magic > 1) {
            magic /= 2;
        }
        return magic;
    }

    @Override
    public float modifyBaseThirdMagic(float magic, AbstractCard card) {
        if (magic > 1) {
            magic /= 2;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        ExtraEffectPatches.EffectFields.mindMeldCount.set(card, ExtraEffectPatches.EffectFields.mindMeldCount.get(card) + 1);
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new KhufoMod();
    }
}
