package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.powers.interfaces.LastDamageTakenUpdatePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class ParryPower extends AbstractEasyPower implements LastDamageTakenUpdatePower {
    public static final String POWER_ID = MainModfile.makeID(ParryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ParryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastTaken) {
        if (info.type == DamageInfo.DamageType.NORMAL && lastTaken == 0) {
            flash();
            addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
        }
    }
}