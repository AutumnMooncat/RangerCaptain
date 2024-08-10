package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AdeptileMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AdeptileMod.class.getSimpleName());
    public static final String DESCRIPTION = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;
    private boolean applyPower;

    public AdeptileMod() {
        this(true);
    }

    public AdeptileMod(boolean applyPower) {
        super(ID, DESCRIPTION, CARD_TEXT[0]);
        this.applyPower = applyPower;
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
    public void onInitialApplication(AbstractCard card) {
        MindMeldPatches.MindMeldField.mindMeldCount.set(card, MindMeldPatches.MindMeldField.mindMeldCount.get(card) + 1);
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (applyPower) {
            return super.modifyDescription(rawDescription, card);
        }
        return rawDescription;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AdeptileMod(applyPower);
    }
}
