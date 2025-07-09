package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractDamageModComponent;
import RangerCaptain.damageMods.PiercingDamage;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.Collections;
import java.util.List;

public class AddPiercingDamageComponent extends AbstractDamageModComponent {
    public static final String ID = MainModfile.makeID(AddPiercingDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddPiercingDamageComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public AbstractDamageModifier getDamageMod(int amount) {
        return new PiercingDamage();
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return CARD_TEXT[0];
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public AbstractComponent makeCopy() {
        return new AddPiercingDamageComponent();
    }
}
