package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.InfusionTriggerAction;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.util.CalcHelper;
import RangerCaptain.util.TextureScaler;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DealDamageMod extends AbstractInfusion {
    public static final String ID = MainModfile.makeID(DealDamageMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final Texture ICON = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/pressure_points"), 64, 64);

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public DealDamageMod(int baseAmount) {
        super(ID, InfusionType.DAMAGE_DIRECT, baseAmount, TEXT[0], ICON);
    }

    public DealDamageMod(int baseAmount, int relicStatsVal) {
        super(ID, InfusionType.DAMAGE_DIRECT, baseAmount, TEXT[0], ICON);
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
        if (target instanceof AbstractMonster) {
            Wiz.atb(new InfusionTriggerAction(this, val, relicStatsVal == baseVal ? val : val - CalcHelper.calculateCardDamage(baseVal - relicStatsVal, (AbstractMonster) target)));
            Wiz.atb(new DamageAction(target, new DamageInfo(Wiz.adp(), val, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
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
        return new DealDamageMod(baseVal, relicStatsVal);
    }
}
