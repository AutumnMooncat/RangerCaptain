package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.powers.interfaces.MonsterOnExhaustPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class BadForecastPower extends AbstractEasyPower implements MonsterOnExhaustPower {
    public static final String POWER_ID = MainModfile.makeID(BadForecastPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final AbstractCreature source;

    public BadForecastPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID, NAME, PowerType.DEBUFF, false, owner, amount);
        this.source = source;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void monsterOnExhaust(AbstractCard card) {
        flashWithoutSound();
        addToBot(new ApplyPowerAction(owner, source, new ToxinPower(owner, amount)));
    }
}