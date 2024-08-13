package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.InfusionTriggerAction;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.util.TextureScaler;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class ApplyPoisonMod extends AbstractInfusion {
    public static final String ID = MainModfile.makeID(ApplyPoisonMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final Texture ICON = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/poison"), 64, 64);

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public ApplyPoisonMod(int baseAmount) {
        super(ID, InfusionType.MAGIC, baseAmount, TEXT[0], ICON);
    }

    public ApplyPoisonMod(int baseAmount, int relicStatsVal) {
        super(ID, InfusionType.MAGIC, baseAmount, TEXT[0], ICON);
        this.relicStatsVal = relicStatsVal;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        super.onInitialApplication(card);
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        Wiz.atb(new InfusionTriggerAction(this, val, relicStatsVal));
        Wiz.atb(new ApplyPowerAction(target, Wiz.adp(), new PoisonPower(target, Wiz.adp(), val)));
    }


    @Override
    public boolean shouldApply(AbstractCard card) {
        if (!usesVanillaTargeting(card)) {
            return false;
        }
        return super.shouldApply(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ApplyPoisonMod(baseVal, relicStatsVal);
    }
}
