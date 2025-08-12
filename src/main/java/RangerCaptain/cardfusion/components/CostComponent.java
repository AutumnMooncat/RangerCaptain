package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.List;

public class CostComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(CostComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public CostComponent(int base) {
        super(ID);
        baseAmount = base;
    }

    @Override
    public void updatePrio() {
        priority = COST_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return false;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.cost = Math.max(card.cost, baseAmount);
        card.costForTurn = Math.max(card.costForTurn, baseAmount);
    }

    @Override
    public String componentDescription() {
        return null;
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return null;
    }

    @Override
    public AbstractComponent makeCopy() {
        return new CostComponent(baseAmount);
    }
}
