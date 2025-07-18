package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SuitUpPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SuitUpPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SuitUpPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }

    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.baseBlock > -1 && !(card instanceof RitualDagger)) {
            flash();
            addToTop(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public float modifyBlockLast(float blockAmount) {
        return blockAmount * 1.5f;
    }
}