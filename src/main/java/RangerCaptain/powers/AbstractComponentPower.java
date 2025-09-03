package RangerCaptain.powers;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.cardfusion.abstracts.AbstractPowerComponent;
import RangerCaptain.cardfusion.components.ExhaustAttacksComponent;
import RangerCaptain.cardfusion.components.ExhaustCardsComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.CalcHelper;
import RangerCaptain.util.Wiz;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractComponentPower extends AbstractEasyPower implements AbstractComponent.ComponentAmountProvider, NonStackablePower {
    protected static final CalcHelper.DummyCard dummyCard = new CalcHelper.DummyCard();
    public AbstractPowerComponent source;
    public List<AbstractComponent> captured;
    private boolean locked;

    public AbstractComponentPower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(ID, NAME, powerType, isTurnBased, owner, amount);
    }

    public AbstractComponentPower(String ID, String NAME, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractPowerComponent source, List<AbstractComponent> captured) {
        super(ID, NAME, powerType, isTurnBased, owner, 1);
        this.source = source;
        this.captured = captured;
        updateDescription();
    }

    public abstract void updateNormalDescription();

    public void triggerComponents(AbstractMonster target, boolean toTop) {
        if (!locked) {
            locked = true;
            boolean couldPass = ReflectionHacks.getPrivateStatic(BindingPatches.class, "canPassInstigator");
            ReflectionHacks.setPrivateStatic(BindingPatches.class, "canPassInstigator", true);
            DamageModifierManager.clearModifiers(dummyCard);
            for (AbstractComponent c : captured) {
                if (c instanceof AbstractDamageModComponent) {
                    DamageModifierManager.addModifier(dummyCard, ((AbstractDamageModComponent) c).getDamageMod(getAmount(c)));
                }
            }
            if (AbstractDungeon.actionManager.currentAction != null) {
                BindingPatches.BoundGameActionFields.actionDelayedCardInUse.set(AbstractDungeon.actionManager.currentAction, dummyCard);
            }
            ActionCapturePatch.doCapture = true;
            ArrayList<AbstractComponent> complex = captured.stream().filter(c -> c.hasFlags(AbstractComponent.Flag.EXHAUST_COMPLEX_FOLLOWUP)).collect(Collectors.toCollection(ArrayList::new));
            for (AbstractComponent component : captured) {
                if (component instanceof AbstractDamageModComponent || component.hasFlags(AbstractComponent.Flag.EXHAUST_COMPLEX_FOLLOWUP)) {
                    continue;
                }
                if (component instanceof ExhaustAttacksComponent || component instanceof ExhaustCardsComponent) {
                    component.onTrigger(this, Wiz.adp(), target, complex);
                } else {
                    component.onTrigger(this, Wiz.adp(), target, Collections.emptyList());
                }
            }
            addToBot(new DoAction(() -> locked = false));
            if (toTop) {
                ActionCapturePatch.releaseToTop();
            } else {
                ActionCapturePatch.releaseToBot();
            }
            ReflectionHacks.setPrivateStatic(BindingPatches.class, "canPassInstigator", couldPass);
        }
    }

    @Override
    public void updateDescription() {
        if (source == null) {
            updateNormalDescription();
        } else {
            for (AbstractComponent c : captured) {
                c.workingAmount *= amount;
            }
            source.workingAmount *= amount;
            this.description = source.rawPowerText(captured);
            source.workingAmount /= amount;
            for (AbstractComponent c : captured) {
                c.workingAmount /= amount;
            }
        }
    }

    @Override
    public int getAmount(AbstractComponent component) {
        return amount * component.workingAmount;
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        if (ID.equals(power.ID) && power instanceof AbstractComponentPower) {
            AbstractComponentPower other = (AbstractComponentPower) power;
            if (source == null && other.source == null) {
                return true;
            }
            if (source != null && captured != null && other.source != null && other.captured != null) {
                return source.functionallyEquals(other.source) && AbstractComponent.functionallyEquivalent(captured, other.captured);
            }
        }
        return false;
    }
}
