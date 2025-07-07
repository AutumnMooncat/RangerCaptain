package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnPlayPerfectComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.List;

public class GlassBondsPower extends AbstractEasyPower implements AbstractComponent.ComponentAmountProvider, NonStackablePower {
    public static final String POWER_ID = MainModfile.makeID(GlassBondsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private OnPlayPerfectComponent source;
    private List<AbstractComponent> captured;

    public GlassBondsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public GlassBondsPower(AbstractCreature owner, String name, OnPlayPerfectComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, 1);
        this.source = source;
        this.captured = captured;
        updateDescription();
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        if (power instanceof GlassBondsPower) {
            GlassBondsPower other = (GlassBondsPower) power;
            if (source == null && other.source == null) {
                return true;
            }
            if (captured != null && other.captured != null) {
                return AbstractComponent.functionallyEquivalent(captured, other.captured);
            }
        }
        return false;
    }

    @Override
    public void updateDescription() {
        if (source == null) {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else {
            for (AbstractComponent c : captured) {
                c.baseAmount *= amount;
            }
            this.description = source.rawPowerText(captured);
            for (AbstractComponent c : captured) {
                c.baseAmount /= amount;
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.get(card)) {
            flash();
            if (source == null) {
                addToTop(new GainBlockAction(owner, amount));
            } else {
                ActionCapturePatch.doCapture = true;
                for (int i = captured.size() - 1; i >= 0; i--) {
                    captured.get(i).onTrigger(this, Wiz.adp(), null, Collections.emptyList());
                }
                ActionCapturePatch.releaseToTop();
            }
        }
    }

    @Override
    public int getAmount(AbstractComponent component) {
        return amount * component.baseAmount;
    }
}