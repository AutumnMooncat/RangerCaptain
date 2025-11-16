package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.MakeCardsComponent;
import RangerCaptain.cardfusion.components.OnExhaustComponent;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.ArrayList;
import java.util.List;

public class FlammablePower extends AbstractComponentPower {
    public static final String POWER_ID = MainModfile.makeID(FlammablePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final ArrayList<Class<?>> banned = new ArrayList<>();

    public FlammablePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public FlammablePower(AbstractCreature owner, String name, OnExhaustComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
        for (AbstractComponent comp : captured) {
            if (comp instanceof MakeCardsComponent && ((MakeCardsComponent) comp).reference != null) {
                banned.add(((MakeCardsComponent) comp).reference.getClass());
            }
        }
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (source == null) {
            flash();
            addToBot(new ApplyPowerToRandomEnemyAction(owner, new BurnedPower(null, owner, amount), amount));
        } else if (banned.stream().noneMatch(clz -> clz == card.getClass())) {
            flash();
            addToBot(new DoAction(() -> triggerComponents(null, true)));
        }
    }
}