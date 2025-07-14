package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnStashPower {
    void onDiscover(AbstractCard card, boolean isEndTurn);
}
