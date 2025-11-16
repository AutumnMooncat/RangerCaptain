package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class PetrifiedPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(PetrifiedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final Color greenColor = new Color(0.0F, 1.0F, 0.0F, 1.0F);
    private int currentReduction;

    public PetrifiedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        //currentReduction = amount;
        updateDescription();
    }

    /*@Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        currentReduction += stackAmount;
        updateDescription();
    }*/

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + currentReduction + DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        greenColor.a = c.a;
        c = greenColor;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(currentReduction), x, y, fontScale, c);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage - currentReduction : damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != owner) {
            flash();
            currentReduction += amount;
            updateDescription();
            AbstractDungeon.onModifyPower();
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}