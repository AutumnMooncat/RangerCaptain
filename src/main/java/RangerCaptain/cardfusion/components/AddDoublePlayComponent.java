package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.ExtraEffectPatches;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddDoublePlayComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(AddDoublePlayComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddDoublePlayComponent() {
        super(ID);
    }

    @Override
    public void updatePrio() {
        priority = SUFFIX_PRIO;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        ExtraEffectPatches.EffectFields.doublePlay.set(card, true);
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
        return new AddDoublePlayComponent();
    }
}
