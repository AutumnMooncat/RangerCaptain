package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddRetainComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(AddRetainComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddRetainComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = PREFIX_PRIO;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other instanceof AddEtherealComponent;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.selfRetain = true;
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
        return new AddRetainComponent();
    }
}
