package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.SilentGainPowerEffect;

import java.util.ArrayList;

public class FervorPower extends AbstractPower {
    public static final String POWER_ID = MainModfile.makeID(FervorPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float flashTimer;
    private float particleTimer;
    private final ArrayList<AbstractGameEffect> array;
    private boolean active;

    public FervorPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadRegion("phantasmal");
        this.priority = 7;
        updateDescription();
        array = ReflectionHacks.getPrivateInherited(this, FervorPower.class, "effect");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onInitialApplication() {
        checkStacks();
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        checkStacks();
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        checkStacks();
    }

    private void checkStacks() {
        boolean wasActive = active;
        active = amount >= 10;
        if (!wasActive && active) {
            CardCrawlGame.sound.play("ATTACK_FIRE");
            AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.FIREBRICK, true));
        }
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (active) {
            flashTimer += Gdx.graphics.getDeltaTime();
        }
        if (flashTimer > 1f) {
            array.add(new SilentGainPowerEffect(this));
            flashTimer = 0f;
        }
    }

    @Override
    public void updateParticles() {
        if (active) {
            particleTimer -= Gdx.graphics.getDeltaTime();
        }
        if (active && particleTimer <= 0) {
            particleTimer = 0.1f;
            for(int i = 0; i < 4; ++i) {
                AbstractDungeon.effectsQueue.add(new FlameParticleEffect(owner.hb.cX, owner.hb.cY));
            }

            for(int i = 0; i < 1; ++i) {
                AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(owner.hb.cX, owner.hb.cY));
            }
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (active && type == DamageInfo.DamageType.NORMAL) {
            return damage * 2f;
        }
        return damage;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (active && card.type == AbstractCard.CardType.ATTACK) {
            addToBot(new ReducePowerAction(owner, owner, this, 10));
        }
    }
}