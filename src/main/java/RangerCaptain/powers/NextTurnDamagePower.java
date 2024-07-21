package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.FormatHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NextTurnDamagePower extends AbstractPower implements NonStackablePower {
    public static final String POWER_ID = MainModfile.makeID(NextTurnDamagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractCreature target;
    public final DamageInfo info;

    public NextTurnDamagePower(AbstractCreature owner, AbstractCreature target, DamageInfo info) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.owner = owner;
        this.amount = info.base;
        this.target = target;
        this.info = info;
        this.type = PowerType.BUFF;
        this.loadRegion("infiniteBlades");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + info.base + DESCRIPTIONS[1] + FormatHelper.prefixWords(target.name, "#y") + DESCRIPTIONS[2];
    }

    @Override
    public void atStartOfTurn() {
        flash();
        addToBot(new DamageAction(target, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}