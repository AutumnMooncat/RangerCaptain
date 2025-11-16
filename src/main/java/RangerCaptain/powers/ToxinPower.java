package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.AttackEffectPatches;
import RangerCaptain.powers.interfaces.OnModifyMaxHPPower;
import RangerCaptain.util.TISHelper;
import RangerCaptain.vfx.ColoredSmokeBombEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.DamageAllButOneEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ToxinPower extends AbstractEasyPower implements HealthBarRenderPower, OnModifyMaxHPPower {
    public static final String POWER_ID = MainModfile.makeID(ToxinPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final Color hpBarColor = Color.PURPLE.cpy();
    private boolean triggered;

    public ToxinPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, true, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_POISON", 0.05F);
    }

    public void delayedCheckIfSurpassedHP() {
        if (triggered) {
            return;
        }
        addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                checkIfSurpassedHP();
                isDone = true;
            }
        });
    }

    public void checkIfSurpassedHP() {
        if (amount >= owner.currentHealth && owner.currentHealth > 0) {
            if (!triggered) {
                triggered = true;
                doKill();
                startFlashing();
            }
        }
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        delayedCheckIfSurpassedHP();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        delayedCheckIfSurpassedHP();

    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            delayedCheckIfSurpassedHP();
        }
        return super.onAttacked(info, damageAmount);
    }

    public void doKill() {
        addToTop(new InstantKillAction(owner));
        addToTop(new VFXAction(new FlashAtkImgEffect(owner.hb.cX, owner.hb.cY, AttackEffectPatches.RANGER_CAPTAIN_TOXIN)));
        //addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        addToTop(new FastShakeAction(owner, 1.0F, 0.25F));
    }

    @Override
    public void onDeath() {
        //addToBot(new VFXAction(new FlashAtkImgEffect(owner.hb.cX, owner.hb.cY, AttackEffectPatches.RANGER_CAPTAIN_TOXIN)));
        addToBot(new VFXAction(new ColoredSmokeBombEffect(owner.hb.cX, owner.hb.cY, Color.PURPLE)));
        if (owner.isPlayer) {
            if (Loader.isModLoaded("spireTogether")) {
                TISHelper.doToxinTeamkill(owner, amount);
            }
        } else if (owner instanceof AbstractMonster){
            addToBot(new DamageAllButOneEnemyAction(owner, owner, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AttackEffectPatches.RANGER_CAPTAIN_TOXIN));
        }
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }

    @Override
    public int onModifyMaxHP(AbstractCreature target, int amount) {
        delayedCheckIfSurpassedHP();
        return amount;
    }
}