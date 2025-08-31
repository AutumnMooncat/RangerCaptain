package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public abstract class AbstractSplitTurnApplyComponent extends AbstractApplyComponent {
    public static final String TEMP_ID = MainModfile.makeID(AbstractSplitTurnApplyComponent.class.getSimpleName());
    public static final String[] TEMP_DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(TEMP_ID).TEXT;
    public static final String[] TEMP_CARD_TEXT = CardCrawlGame.languagePack.getUIString(TEMP_ID).EXTRA_TEXT;

    public AbstractSplitTurnApplyComponent(String ID, float base, ComponentTarget target) {
        super(ID, base, target);
        this.isSimple = true;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return false;
    }

    @Override
    public String componentDescription() {
        return String.format(TEMP_DESCRIPTION_TEXT[target.ordinal()], getName());
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(TEMP_CARD_TEXT[target.ordinal()], dynKey(), getKeyword());
    }

    @Override
    public String rawCapturedText() {
        return String.format(TEMP_CARD_TEXT[ComponentTarget.values().length], dynKey(), getKeyword());
    }
}
