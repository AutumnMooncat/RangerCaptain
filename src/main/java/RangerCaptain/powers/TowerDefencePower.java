package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnGainBlockComponent;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnCreateBlockInstancePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import java.util.HashSet;
import java.util.List;

public class TowerDefencePower extends AbstractComponentPower implements OnCreateBlockInstancePower {
    public static final String POWER_ID = MainModfile.makeID(TowerDefencePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final float MULTI = 0.5f;

    public TowerDefencePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public TowerDefencePower(AbstractCreature owner, String name, OnGainBlockComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else if (amount == 2) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[2] + (amount*MULTI) + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onCreateBlockInstance(HashSet<AbstractBlockModifier> hashSet, Object o) {
        if (o instanceof AbstractCard && ((AbstractCard) o).block > 1) {
            flash();
            if (source == null) {
                addToBot(new ApplyPowerAction(owner, owner, new NextTurnBlockPower(owner, (int) (((AbstractCard) o).block * amount * MULTI)), (int) (((AbstractCard) o).block * amount * MULTI), true));
            } else {
                triggerComponents(null, false);
            }
        }
    }
}