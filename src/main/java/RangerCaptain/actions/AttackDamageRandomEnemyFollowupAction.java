package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.function.Consumer;

public class AttackDamageRandomEnemyFollowupAction extends AttackDamageRandomEnemyAction {
    private final Consumer<AbstractCreature> followup;

    public AttackDamageRandomEnemyFollowupAction(AbstractCard card, AttackEffect effect, Consumer<AbstractCreature> followup) {
        super(card, effect);
        this.followup = followup;
    }

    public AttackDamageRandomEnemyFollowupAction(AbstractCard card, Consumer<AbstractCreature> followup) {
        super(card);
        this.followup = followup;
    }


    @Override
    public void update() {
        super.update();
        followup.accept(this.target);
    }
}
