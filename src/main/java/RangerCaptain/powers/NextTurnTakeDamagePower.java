package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.LockedDamageInfoPatches;
import RangerCaptain.powers.interfaces.MonsterAtPlayerStartOfTurnPower;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class NextTurnTakeDamagePower extends AbstractEasyPower implements NonStackablePower, MonsterAtPlayerStartOfTurnPower {
    public static final String POWER_ID = MainModfile.makeID(NextTurnTakeDamagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractGameAction.AttackEffect effect;
    public final DamageInfo info;

    public NextTurnTakeDamagePower(AbstractCreature owner, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        super(POWER_ID, NAME, NeutralPowertypePatch.NEUTRAL, false, owner, info.base);
        this.info = info;
        this.effect = effect;
        updateDescription();
        LockedDamageInfoPatches.LockedField.lockedDamage.set(this.info, this.info.base);
    }

    @Override
    public void updateDescription() {
        if (info == null) {
            this.description = "???";
        } else {
            this.description = DESCRIPTIONS[0] + info.base + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atPlayerStartOfTurn() {
        flash();
        addToBot(new DamageAction(owner, info, effect));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}