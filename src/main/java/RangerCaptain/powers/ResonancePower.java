package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

public class ResonancePower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(ResonancePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final AbstractCreature source;
    protected Color greenColor2;
    private int hits;

    public ResonancePower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
        this.greenColor2 = Color.GREEN.cpy();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL) {
            hits++;
            if (hits == 2) {
                startFlashing();
            } else if (hits == 3) {
                hits = 0;
                stopFlashing();
                addToTop(new ReducePowerAction(owner, owner, this, amount));
                addToTop(new DoAction(() -> {
                    for(int i = 0; i < 15; ++i) {
                        AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(owner.hb.cX + MathUtils.random(-20.0F, 20.0F) * Settings.scale, owner.hb.cY + MathUtils.random(-20.0F, 20.0F) * Settings.scale));
                    }
                }));
                addToTop(new DamageAction(owner, new DamageInfo(source, 10*amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
            }
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atEndOfRound() {
        hits = 0;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + 10*amount + DESCRIPTIONS[1];
    }

    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        if (hits != 0) {
            this.greenColor2.a = c.a;
            c = this.greenColor2;
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(hits), x, y + 15.0F * Settings.scale, this.fontScale, c);
        }
    }
}