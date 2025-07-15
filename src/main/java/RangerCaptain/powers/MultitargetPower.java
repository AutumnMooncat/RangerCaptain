package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class MultitargetPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(MultitargetPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MultitargetPower(AbstractCreature owner, int amount) {
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
        if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            if (card.type == AbstractCard.CardType.ATTACK && action.target != null) {
                flash();
                Wiz.forAllMonstersLiving(mon -> {
                    if (mon != action.target) {
                        card.calculateCardDamage(mon);
                        card.use(Wiz.adp(), mon);
                    }
                });
                addToTop(new ReducePowerAction(owner, owner, this, 1));
            }
        }
    }
}