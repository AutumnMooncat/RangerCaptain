package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnDiscoverPower {
    void onDiscover(AbstractCard card, boolean isEndTurn);
}
