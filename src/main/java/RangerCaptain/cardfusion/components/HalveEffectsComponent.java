package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractTraitComponent;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class HalveEffectsComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(HalveEffectsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public HalveEffectsComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = MODIFIER_PRIO;
    }

    @Override
    public boolean modifiesAmount(AbstractComponent other) {
        return other.makesActions();
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 0.5f;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return null;
    }

    @Override
    public AbstractComponent makeCopy() {
        return new HalveEffectsComponent();
    }
}
