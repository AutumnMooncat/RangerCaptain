package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SureFirePower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SureFirePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SureFirePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (target != owner && info.type == DamageInfo.DamageType.NORMAL) {
            flash();
            addToTop(new ApplyPowerAction(target, owner, new BurnedPower(target, owner, amount), amount, true));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}