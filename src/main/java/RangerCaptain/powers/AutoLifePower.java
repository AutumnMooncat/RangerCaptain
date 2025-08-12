package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.HealComponent;
import RangerCaptain.cardfusion.components.OnDieComponent;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;

public class AutoLifePower extends AbstractComponentPower implements OnPlayerDeathPower {
    public static final String POWER_ID = MainModfile.makeID(AutoLifePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AutoLifePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public AutoLifePower(AbstractCreature owner, String name, OnDieComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onVictory() {
        if (source == null) {
            owner.heal(amount);
        } else {
            for (AbstractComponent component : captured) {
                if (component instanceof HealComponent) {
                    owner.heal(component.baseAmount * amount);
                }
            }
        }
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        if (source == null) {
            owner.heal(amount);
        } else {
            for (AbstractComponent component : captured) {
                if (component instanceof HealComponent) {
                    owner.heal(component.baseAmount * amount);
                    ((HealComponent) component).alreadyPerformed = true;
                }
            }
            triggerComponents(null, true);
        }
        addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        return false;
    }
}