package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DamageRandomPredicateEnemyFollowupAction extends AbstractGameAction {
    private final DamageInfo info;
    private final Predicate<AbstractMonster> predicate;
    private final Consumer<AbstractCreature> followup;

    public DamageRandomPredicateEnemyFollowupAction(DamageInfo info, AttackEffect effect, Predicate<AbstractMonster> predicate, Consumer<AbstractCreature> followup) {
        this.info = info;
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.predicate = predicate;
        this.followup = followup;
    }

    @Override
    public void update() {
        ArrayList<AbstractMonster> valid = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && predicate.test(monster)) {
                valid.add(monster);
            }
        }
        if (!valid.isEmpty()) {
            target = valid.get(AbstractDungeon.cardRandomRng.random(0, valid.size()-1));
            addToTop(new DamageFollowupAction(target, info, attackEffect, false, followup));
        }
        isDone = true;
    }
}
