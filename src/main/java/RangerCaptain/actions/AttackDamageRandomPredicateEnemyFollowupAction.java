package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AttackDamageRandomPredicateEnemyFollowupAction extends AbstractGameAction {
    private final AbstractCard card;
    private final AbstractGameAction.AttackEffect effect;
    private final Predicate<AbstractMonster> predicate;
    private final Consumer<AbstractCreature> followup;

    public AttackDamageRandomPredicateEnemyFollowupAction(AbstractCard card, AttackEffect effect, Predicate<AbstractMonster> predicate, Consumer<AbstractCreature> followup) {
        this.card = card;
        this.effect = effect;
        this.predicate = predicate;
        this.followup = followup;
    }

    public AttackDamageRandomPredicateEnemyFollowupAction(AbstractCard card, Predicate<AbstractMonster> predicate, Consumer<AbstractCreature> followup) {
        this(card, AttackEffect.NONE, predicate, followup);
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
            card.calculateCardDamage((AbstractMonster)target);
            if (AttackEffect.LIGHTNING == effect) {
                addToTop(new DamageFollowupAction(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), AttackEffect.NONE, false, followup));
                addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                addToTop(new VFXAction(new LightningEffect(target.hb.cX, target.hb.cY)));
            } else {
                addToTop(new DamageFollowupAction(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), effect, false, followup));
            }
        }

        this.isDone = true;
    }
}
