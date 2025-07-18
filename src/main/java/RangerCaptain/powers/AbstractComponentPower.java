package RangerCaptain.powers;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractPowerComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.CalcHelper;
import RangerCaptain.util.Wiz;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.BindingPatches;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.List;

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
            ActionCapturePatch.doCapture = true;
            ActionCapturePatch.onCapture = this::captureCheck;
            for (AbstractComponent component : captured) {
                component.onTrigger(this, Wiz.adp(), target, Collections.emptyList());
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

    public void captureCheck(AbstractGameAction action) {
        if (!(action instanceof ApplyPowerAction)) {
            BindingPatches.BoundGameActionFields.actionDelayedCardInUse.set(action, dummyCard);
        }
    }

    @Override
    public void updateDescription() {
        if (source == null) {
            updateNormalDescription();
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
    public int getAmount(AbstractComponent component) {
        return amount * component.baseAmount;
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
