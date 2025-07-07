package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.MainModfile;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public abstract class AbstractTempApplyComponent extends AbstractApplyComponent {
    public static final String TEMP_ID = MainModfile.makeID(AbstractTempApplyComponent.class.getSimpleName());
    public static final String[] TEMP_DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(TEMP_ID).TEXT;
    public static final String[] TEMP_CARD_TEXT = CardCrawlGame.languagePack.getUIString(TEMP_ID).EXTRA_TEXT;
    public boolean increase;

    public AbstractTempApplyComponent(String ID, int base, ComponentTarget target, boolean increase) {
        super(ID, base, target);
        this.increase = increase;
        this.isSimple = true;
    }

    @Override
    public String componentDescription() {
        if (increase) {
            return String.format(TEMP_DESCRIPTION_TEXT[target.ordinal()], getName());
        } else {
            return String.format(TEMP_DESCRIPTION_TEXT[target.ordinal() + ComponentTarget.values().length + 1], getName());
        }
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (increase) {
            return String.format(TEMP_CARD_TEXT[target.ordinal()], dynKey(), getKeyword());
        } else {
            return String.format(TEMP_CARD_TEXT[target.ordinal() + ComponentTarget.values().length + 1], dynKey(), getKeyword());
        }
    }

    @Override
    public String rawCapturedText() {
        if (increase) {
            return String.format(TEMP_CARD_TEXT[ComponentTarget.values().length], dynKey(), getKeyword());
        } else {
            return String.format(TEMP_CARD_TEXT[ComponentTarget.values().length * 2 + 1], dynKey(), getKeyword());
        }
    }
}
