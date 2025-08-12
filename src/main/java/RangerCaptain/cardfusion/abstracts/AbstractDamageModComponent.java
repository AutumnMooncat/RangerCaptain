package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.cards.tokens.FusedCard;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public abstract class AbstractDamageModComponent extends AbstractComponent {
    public AbstractDamageModComponent(String ID) {
        this(ID, -1);
        setFlags(Flag.REQUIRES_DAMAGE);
    }

    public AbstractDamageModComponent(String ID, float base) {
        super(ID, base, ComponentType.MODIFIER, ComponentTarget.NONE, base == -1 ? DynVar.NONE : DynVar.FLAT);
    }

    public abstract AbstractDamageModifier getDamageMod(int amount);

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        DamageModifierManager.addModifier(card, getDamageMod(workingAmount));
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return identifier().equals(other.identifier());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) { }
}
