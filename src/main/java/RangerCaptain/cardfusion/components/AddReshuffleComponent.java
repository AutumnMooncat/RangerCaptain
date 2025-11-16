package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddReshuffleComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(AddReshuffleComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddReshuffleComponent() {
        super(ID);
        setFlags(Flag.REMOVE_IF_POWER, Flag.REMOVE_IF_EXHAUST);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.shuffleBackIntoDrawPile = true;
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
    public AbstractComponent makeCopy() {
        return new AddReshuffleComponent();
    }
}
