package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnParryComponent;
import RangerCaptain.powers.interfaces.LastDamageTakenUpdatePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.List;

public class ParryPower extends AbstractComponentPower implements LastDamageTakenUpdatePower {
    public static final String POWER_ID = MainModfile.makeID(ParryPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ParryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public ParryPower(AbstractCreature owner, String name, OnParryComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onLastDamageTakenUpdate(DamageInfo info, int lastTaken) {
        if (info.type == DamageInfo.DamageType.NORMAL && lastTaken == 0 && info.owner instanceof AbstractMonster) {
            flash();
            if (source == null) {
                addToTop(new ApplyPowerAction(owner, owner, new VigorPower(owner, amount)));
            } else {
                triggerComponents((AbstractMonster) info.owner, true);
            }
        }
    }
}