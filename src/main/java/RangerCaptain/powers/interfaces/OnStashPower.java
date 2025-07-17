package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnStashPower {
    void onStash(AbstractCard card, boolean isEndTurn);
}
