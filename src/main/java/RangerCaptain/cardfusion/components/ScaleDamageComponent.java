package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.AbstractComponentPower;
import RangerCaptain.util.FormatHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class ScaleDamageComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(ScaleDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public boolean pluralize;

    public ScaleDamageComponent() {
        this(0);
    }

    public ScaleDamageComponent(int base) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, base == 0 ? DynVar.NONE : DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof ScaleDamageComponent) {
            return dynvar == other.dynvar;
        }
        return false;
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (baseAmount == 0) {
            return pluralize ? CARD_TEXT[2] : CARD_TEXT[1];
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        if (dynvar == DynVar.NONE) {
            if (ExhaustAttacksComponent.lastExhausted != null) {
                amount = ExhaustAttacksComponent.lastExhausted.damage;
            } else {
                return;
            }
        }
        int finalAmount = amount;
        if (provider instanceof FusedCard) {
            addToBot(new DoAction(() -> {
                ((FusedCard) provider).baseDamage += finalAmount;
                if (((FusedCard) provider).baseSecondDamage > 0) {
                    ((FusedCard) provider).baseSecondDamage += finalAmount;
                }
            }));
        } else if (provider instanceof AbstractComponentPower){
            addToBot(new DoAction(() -> {
                for (AbstractComponent component : ((AbstractComponentPower) provider).captured) {
                    if (component.type == ComponentType.DAMAGE) {
                        component.baseAmount += finalAmount;
                    }
                }
                ((AbstractComponentPower) provider).updateDescription();
            }));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ScaleDamageComponent(baseAmount);
    }
}
