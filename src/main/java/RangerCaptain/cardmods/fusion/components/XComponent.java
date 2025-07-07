package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class XComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(XComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public XComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.cost = card.costForTurn = -1;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String onBuildText(AbstractComponent other, String text) {
        return injectXOnDynvars(text);
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return null;
    }

    @Override
    public AbstractComponent makeCopy() {
        return new XComponent();
    }
}
