package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnTurnStartForEachAttackerComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.List;

public class BerserkerPower extends AbstractComponentPower {
    public static final String POWER_ID = MainModfile.makeID(BerserkerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BerserkerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public BerserkerPower(AbstractCreature owner, String name, OnTurnStartForEachAttackerComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        boolean[] shouldFlash = {false};
        Wiz.forAllMonstersLiving(mon -> {
            if (mon.getIntentBaseDmg() >= 0) {
                shouldFlash[0] = true;
                if (source == null) {
                    addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
                } else {
                    addToBot(new DoAction(() -> triggerComponents(null, true)));
                }
            }
        });
        if (shouldFlash[0]) {
            flash();
        }
    }
}