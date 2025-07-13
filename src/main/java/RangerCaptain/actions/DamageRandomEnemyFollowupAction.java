package RangerCaptain.actions;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.function.Consumer;

public class DamageRandomEnemyFollowupAction extends DamageRandomPredicateEnemyFollowupAction {
    public DamageRandomEnemyFollowupAction(DamageInfo info, AttackEffect effect, Consumer<AbstractCreature> followup) {
        super(info, effect, m -> true, followup);
    }
}
