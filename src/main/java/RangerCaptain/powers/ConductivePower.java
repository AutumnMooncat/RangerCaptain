package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

public class ConductivePower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(ConductivePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final AbstractCreature source;

    public ConductivePower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            flash();
            addToTop(new ReducePowerAction(owner, owner, this, 1));
            addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
            addToTop(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
            addToTop(new VFXAction(new LightningOrbActivateEffect(owner.hb.cX, owner.hb.cY)));
        }
        return damageAmount;
    }
}