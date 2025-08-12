package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.EnergyOnKillDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.Collections;
import java.util.List;

public class AddEnergyOnKillDamageComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(AddEnergyOnKillDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddEnergyOnKillDamageComponent(int base) {
        super(ID, base);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new EnergyOnKillDamage(amount);
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (baseAmount >= 1 && baseAmount <= 3) {
            return CARD_TEXT[baseAmount];
        }
        return String.format(CARD_TEXT[0], baseAmount);
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public AbstractComponent makeCopy() {
        return new AddEnergyOnKillDamageComponent(baseAmount);
    }
}
