package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.EnergyGainPatch;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class EnergyReservesPower extends AbstractEasyPower implements EnergyGainPatch.OnGainEnergyPower {
    public static final String POWER_ID = MainModfile.makeID(EnergyReservesPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public EnergyReservesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onGainEnergy(int amount) {
        flash();
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, owner, this.amount)));
        return amount;
    }
}