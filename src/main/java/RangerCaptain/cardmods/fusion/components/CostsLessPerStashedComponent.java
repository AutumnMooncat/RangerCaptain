package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.CardCounterPatches;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class CostsLessPerStashedComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(CostsLessPerStashedComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public CostsLessPerStashedComponent() {
        super(ID, 0, ComponentType.MODIFIER, ComponentTarget.NONE, DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = PREFIX_PRIO;
    }

    @Override
    public boolean scalesWithCost() {
        return false;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof CostsLessPerStashedComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {}

    @Override
    public void triggerOnOtherCardStashed(FusedCard card, AbstractCard stashed) {
        card.setCostForTurn(card.costForTurn - card.getAmount(this));
    }

    @Override
    public void triggerWhenDrawn(FusedCard card) {
        card.setCostForTurn(card.cost - (CardCounterPatches.cardsStashedThisTurn * card.getAmount(this)));
    }

    @Override
    public void atTurnStart(FusedCard card) {
        card.resetAttributes();
        card.applyPowers();
    }

    @Override
    public void onMakeCopy(FusedCard card, FusedCard copy) {
        copy.setCostForTurn(copy.cost - (CardCounterPatches.cardsStashedThisTurn * card.getAmount(this)));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new CostsLessPerStashedComponent();
    }
}
