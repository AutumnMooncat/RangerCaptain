package RangerCaptain.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BetterTransformCardInHandAction extends AbstractGameAction {
    private final AbstractCard targetCard;
    private final AbstractCard replacement;

    public BetterTransformCardInHandAction(AbstractCard targetCard, AbstractCard replacement) {
        this.targetCard = targetCard;
        this.replacement = replacement;// 15
        if (Settings.FAST_MODE) {// 17
            this.startDuration = 0.05F;// 18
        } else {
            this.startDuration = 0.15F;// 20
        }

        this.duration = this.startDuration;// 22
    }// 23

    public void update() {
        if (!AbstractDungeon.player.hand.contains(targetCard)) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startDuration) {// 27
            int handIndex = AbstractDungeon.player.hand.group.indexOf(targetCard);
            this.replacement.current_x = targetCard.current_x;// 29
            this.replacement.current_y = targetCard.current_y;// 30
            this.replacement.target_x = targetCard.target_x;// 31
            this.replacement.target_y = targetCard.target_y;// 32
            this.replacement.drawScale = 1.0F;// 33
            this.replacement.targetDrawScale = targetCard.targetDrawScale;// 34
            this.replacement.angle = targetCard.angle;// 35
            this.replacement.targetAngle = targetCard.targetAngle;// 36
            this.replacement.superFlash(Color.WHITE.cpy());// 37
            AbstractDungeon.player.hand.group.set(handIndex, this.replacement);// 38
            AbstractDungeon.player.hand.glowCheck();// 39
        }

        this.tickDuration();// 41
    }// 42
}
