package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddExhaustComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(AddExhaustComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddExhaustComponent() {
        super(ID);
        setFlags(Flag.REMOVE_IF_POWER);
    }

    @Override
    public void updatePrio() {
        priority = SUFFIX_PRIO + 1;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.exhaust = true;
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
        return new AddExhaustComponent();
    }
}
