package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.damageMods.BootDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddBootDamageComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(AddBootDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddBootDamageComponent() {
        super(ID, 0);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {}

    @Override
    public void postAssignment(FusedCard card, List<AbstractComponent> otherComponents) {
        for (AbstractComponent other : otherComponents) {
            if (other.type == ComponentType.DAMAGE) {
                workingAmount = Math.max(workingAmount, other.workingAmount);
            }
        }
        if (workingAmount != 0) {
            DamageModifierManager.addModifier(card, getDamageMod(workingAmount));
        }
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new BootDamage(amount);
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[0], workingAmount -1, workingAmount);
    }

    @Override
    public AbstractComponent makeCopy() {
        return new AddBootDamageComponent();
    }
}
