package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.OnGainEnergyComponent;
import RangerCaptain.patches.OnChangeEnergyPatches;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.List;

public class EnergyReservesPower extends AbstractComponentPower implements OnChangeEnergyPatches.OnChangeEnergyObject {
    public static final String POWER_ID = MainModfile.makeID(EnergyReservesPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public EnergyReservesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public EnergyReservesPower(AbstractCreature owner, String name, OnGainEnergyComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public int onGainEnergy(int amount) {
        energyTrigger();
        return amount;
    }

    @Override
    public int onSetEnergy(int amount) {
        if (amount > EnergyPanel.totalCount) {
            energyTrigger();
        }
        return amount;
    }

    private void energyTrigger() {
        flash();
        if (source == null) {
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, owner, this.amount)));
        } else {
            triggerComponents(null, false);
        }
    }
}