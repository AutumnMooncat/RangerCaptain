package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.OnDrawUnplayableComponent;
import RangerCaptain.patches.EnterCardGroupPatches;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;

public class SpringLoadedPower extends AbstractComponentPower implements EnterCardGroupPatches.OnEnterCardGroupPower {
    public static final String POWER_ID = MainModfile.makeID(SpringLoadedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SpringLoadedPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public SpringLoadedPower(AbstractCreature owner, String name, OnDrawUnplayableComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onEnter(CardGroup g, AbstractCard c) {
        if (g == Wiz.adp().hand && c.cost == -2) {
            flashWithoutSound();
            if (source == null) {
                addToBot(new DrawCardAction(amount));
            } else {
                addToBot(new DoAction(() -> triggerComponents(null, true)));
            }
        }
    }
}