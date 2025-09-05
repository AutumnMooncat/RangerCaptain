package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.OnTurnStartComponent;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;

public class AcidReflexPower extends AbstractComponentPower {
    public static final String POWER_ID = MainModfile.makeID(AcidReflexPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AcidReflexPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount); // TODO fires before cards drawn so Exhaust fails
    }

    public AcidReflexPower(AbstractCreature owner, String name, OnTurnStartComponent source, List<AbstractComponent> components) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, components);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            if (source == null) {
                addToBot(new ApplyPowerToRandomEnemyAction(owner, new ToxinPower(null, amount), amount));
            } else {
                addToBot(new DoAction(() -> triggerComponents(null, true)));
            }
        }
    }
}