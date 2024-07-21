package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.function.BiConsumer;

public class ProcessDamageRandomEnemyAction extends AbstractGameAction {
    private final AbstractCard card;
    private final BiConsumer<AbstractMonster, Integer> followup;

    public ProcessDamageRandomEnemyAction(AbstractCard card, BiConsumer<AbstractMonster, Integer> followup) {
        this.source = AbstractDungeon.player;
        this.card = card;
        this.followup = followup;
    }// 20

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            this.card.calculateCardDamage((AbstractMonster)this.target);
            followup.accept((AbstractMonster) target, card.damage);
        }
        this.isDone = true;
    }
}