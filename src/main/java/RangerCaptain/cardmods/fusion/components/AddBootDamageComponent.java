package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.damageMods.BootDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.Collections;
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
                baseAmount = Math.max(baseAmount, other.baseAmount);
            }
        }
        if (baseAmount != 0) {
            DamageModifierManager.addModifier(card, getDamageMod(baseAmount));
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
        return String.format(CARD_TEXT[0], baseAmount-1, baseAmount);
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public AbstractComponent makeCopy() {
        return new AddBootDamageComponent();
    }
}
