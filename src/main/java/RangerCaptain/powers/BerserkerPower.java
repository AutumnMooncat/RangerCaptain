package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BerserkerPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(BerserkerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BerserkerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean[] shouldFlash = {false};
        Wiz.forAllMonstersLiving(mon -> {
            if (mon.getIntentBaseDmg() >= 0) {
                shouldFlash[0] = true;
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
            }
        });
        if (shouldFlash[0]) {
            flash();
        }
    }
}