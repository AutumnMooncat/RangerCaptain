package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.ExtraEffectPatches;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class AddCloseEncounterComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(AddCloseEncounterComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddCloseEncounterComponent() {
        super(ID);
        setFlags(Flag.REMOVE_IF_POWER);
    }

    @Override
    public void updatePrio() {
        priority = SUFFIX_PRIO;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        ExtraEffectPatches.EffectFields.closeEncounter.set(card, true);
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
        return new AddCloseEncounterComponent();
    }
}
