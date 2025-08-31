package RangerCaptain.actions;

import RangerCaptain.ui.StashedCardManager;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class StashTempCardAction extends MakeTempCardInDiscardAction {
    public StashTempCardAction(AbstractCard card, int amount) {
        super(card, amount);
    }

    public StashTempCardAction(AbstractCard card, boolean sameUUID) {
        super(card, sameUUID);
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            int amount = ReflectionHacks.getPrivateInherited(this, StashTempCardAction.class, "numCards");
            for (int i = 0; i < amount; i++) {
                StashedCardManager.addCard(makeNewCard());
            }
        }
        tickDuration();
    }

    @SpireOverride
    public AbstractCard makeNewCard() {
        return SpireSuper.call();
    }
}
