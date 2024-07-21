package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.AttackEffectPatches;
import RangerCaptain.powers.interfaces.OnBleedPower;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BleedingPower extends AbstractPower implements HealthBarRenderPower {
    public static final String POWER_ID = MainModfile.makeID(BleedingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final Color hpBarColor = new Color(0.4f, 0f, 0f, 1f);
    private final AbstractCreature source;

    public BleedingPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.loadRegion("brutality");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != owner && info.type == DamageInfo.DamageType.NORMAL) {
            addToTop(new DamageAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AttackEffectPatches.RANGER_CAPTAIN_BLEED));
            for (AbstractPower pow : source.powers) {
                if (pow instanceof OnBleedPower) {
                    ((OnBleedPower) pow).onBleed(owner, amount);
                }
            }
        }
    }

    @Override
    public int getHealthBarAmount() {
        if (owner instanceof AbstractMonster) {
            if (((AbstractMonster) owner).getIntentBaseDmg() > 0) {
                if (ReflectionHacks.<Boolean>getPrivate(owner, AbstractMonster.class, "isMultiDmg")) {
                    return amount * ReflectionHacks.<Integer>getPrivate(owner, AbstractMonster.class, "intentMultiAmt");
                }
            } else {
                return 0;
            }
        }
        return amount;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }
}