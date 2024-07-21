package RangerCaptain.actions;

import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;

public class CorruptAction extends DoAction {
    public CorruptAction() {
        super(() -> {
            CardCrawlGame.sound.play("ORB_DARK_EVOKE", 0.1F);// 74
            AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(Wiz.adp().hb.cX, Wiz.adp().hb.cY));
            //TODO Corrupt consumes Light Charges?
            for (AbstractCard c : Wiz.adp().hand.group) {
                if (c instanceof Miracle) {
                    Wiz.att(new ExhaustSpecificCardAction(c, Wiz.adp().hand));
                    return;
                }
            }
            Wiz.att(new MakeTempCardInDrawPileAction(new VoidCard(), 1, false, true, false));
        });
    }
}
