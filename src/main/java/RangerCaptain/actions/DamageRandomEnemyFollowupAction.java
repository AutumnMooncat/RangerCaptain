package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.function.Consumer;

public class DamageRandomEnemyFollowupAction extends DamageRandomEnemyAction {
    private final Consumer<AbstractCreature> followup;
    public DamageRandomEnemyFollowupAction(DamageInfo info, AttackEffect effect, Consumer<AbstractCreature> followup) {
        super(info, effect);
        this.followup = followup;
    }

    @Override
    public void update() {
        super.update();
        followup.accept(this.target);
    }
}
