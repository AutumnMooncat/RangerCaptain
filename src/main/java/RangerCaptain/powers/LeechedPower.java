package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DamageFollowupAction;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class LeechedPower extends AbstractEasyPower implements HealthBarRenderPower {
    public static final String POWER_ID = MainModfile.makeID(LeechedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final Color hpBarColor = new Color(0.2f, 1f, 0.2f, 1f);
    private final AbstractCreature source;

    public LeechedPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flashWithoutSound();
            addToBot(new DamageFollowupAction(owner, new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON, false, mon -> addToTop(new AddTemporaryHPAction(source, owner, owner.lastDamageTaken))));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return hpBarColor;
    }
}