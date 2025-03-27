package RangerCaptain.actions;

import RangerCaptain.powers.BurnedPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurnLoseHpAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public BurnLoseHpAction(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction.AttackEffect effect) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            isDone = true;
        } else {
            if (duration == DURATION && target.currentHealth > 0) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            }

            tickDuration();
            if (isDone) {
                if (target.currentHealth > 0) {
                    target.tint.color = Color.ORANGE.cpy();
                    target.tint.changeColor(Color.WHITE.cpy());
                    target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));
                }

                AbstractPower p = target.getPower(BurnedPower.POWER_ID);
                if (p != null) {
                    p.amount--;
                    if (p.amount == 0) {
                        target.powers.remove(p);
                    } else {
                        p.updateDescription();
                    }
                }

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                addToTop(new WaitAction(0.1F));
            }

        }
    }
}
