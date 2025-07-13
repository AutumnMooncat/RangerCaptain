package RangerCaptain.actions;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.function.Consumer;

public class AttackDamageRandomEnemyFollowupAction extends AttackDamageRandomPredicateEnemyFollowupAction {

    public AttackDamageRandomEnemyFollowupAction(AbstractCard card, AttackEffect effect, Consumer<AbstractCreature> followup) {
        super(card, effect, m -> true, followup);
    }

    public AttackDamageRandomEnemyFollowupAction(AbstractCard card, Consumer<AbstractCreature> followup) {
        super(card, m -> true, followup);
    }
}
