package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.powers.interfaces.MonsterOnExhaustPower;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class BurnedPower extends AbstractEasyPower implements HealthBarRenderPower, MonsterOnExhaustPower {
    public static final String POWER_ID = MainModfile.makeID(BurnedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final Color hpBarColor = Color.ORANGE.cpy();
    private final AbstractCreature source;

    public BurnedPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
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
    public void monsterOnExhaust(AbstractCard card) {
        flash();
        addToBot(new LoseHPAction(owner, source, amount, AbstractGameAction.AttackEffect.FIRE));
    }
}