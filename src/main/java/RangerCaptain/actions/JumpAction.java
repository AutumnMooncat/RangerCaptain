package RangerCaptain.actions;

import RangerCaptain.cardmods.CarrotMod;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.powers.interfaces.OnJumpPower;
import RangerCaptain.util.Wiz;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class JumpAction extends AbstractGameAction {
    public static ArrayList<AbstractCard> drawnCards = new ArrayList<>();
    private boolean clearQueue;

    public JumpAction(int amount) {
        this(amount, true);
    }

    private JumpAction(int amount, boolean clearQueue) {
        this.amount = amount;
        this.clearQueue = clearQueue;
    }

    @Override
    public void update() {
        if (clearQueue) {
            drawnCards.clear();
            AbstractDungeon.player.useJumpAnimation();
        }
        CardCounterPatches.jumpsThisTurn += amount;
        addToTop(new DrawCardAction(amount, new DoAction(() -> {
            int draw = 0;
            for (AbstractCard card : DrawCardAction.drawnCards) {
                drawnCards.add(card);
                if (CardModifierManager.hasModifier(card, CarrotMod.ID)) {
                    draw++;
                }
                for (AbstractPower pow : Wiz.adp().powers) {
                    if (pow instanceof OnJumpPower) {
                        ((OnJumpPower) pow).onJump(card);
                    }
                }
            }
            if (draw > 0) {
                addToTop(new JumpAction(draw, false));
            }
        })));
        this.isDone = true;
    }
}
