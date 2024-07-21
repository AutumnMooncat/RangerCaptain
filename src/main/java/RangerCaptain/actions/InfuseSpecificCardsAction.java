package RangerCaptain.actions;

import RangerCaptain.cardmods.AbstractInfusion;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InfuseSpecificCardsAction extends AbstractGameAction {
    public static Predicate<AbstractCard> shenaniganFilter = c -> AbstractInfusion.usesVanillaTargeting(c) && c.costForTurn > -2;
    private final List<AbstractCard> cards;
    private final AbstractCardModifier mod;
    private final Predicate<AbstractCard> filter;

    public InfuseSpecificCardsAction(List<AbstractCard> cards, AbstractCardModifier mod) {
        this(cards, mod, c -> true);
    }

    public InfuseSpecificCardsAction(List<AbstractCard> cards, AbstractCardModifier mod, Predicate<AbstractCard> filter) {
        this.cards = cards;
        this.mod = mod;
        this.filter = filter.and(shenaniganFilter);
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> validCards = cards.stream().filter(filter).collect(Collectors.toCollection(ArrayList::new));
        for (AbstractCard c : validCards) {
            InfuseCardsInHandAction.doInfusion(c, mod);
        }
        InfuseCardsInHandAction.infusionSFX();
        this.isDone = true;
    }
}
