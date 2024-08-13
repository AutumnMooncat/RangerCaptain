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
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Arrays;

public class DealAOEDamageMod extends AbstractInfusion {
    public static final String ID = MainModfile.makeID(DealAOEDamageMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final Texture ICON = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/swivel"), 64, 64);

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public DealAOEDamageMod(int baseAmount) {
        super(ID, InfusionType.DAMAGE_ALL, baseAmount, TEXT[0], ICON);
    }

    public DealAOEDamageMod(int baseAmount, int relicStatsVal) {
        super(ID, InfusionType.DAMAGE_ALL, baseAmount, TEXT[0], ICON);
        this.relicStatsVal = relicStatsVal;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        int sum = Arrays.stream(multiVal).sum();
        int[] delta = CalcHelper.calculateCardDamageMulti(baseVal - relicStatsVal);
        int dSum = sum - Arrays.stream(delta).sum();
        Wiz.atb(new InfusionTriggerAction(this, sum, dSum));
        Wiz.atb(new DamageAllEnemiesAction(Wiz.adp(), multiVal, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new DealAOEDamageMod(baseVal, relicStatsVal);
    }
}
