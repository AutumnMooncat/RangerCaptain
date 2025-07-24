package RangerCaptain.relics.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnStashRelic {
    void onStash(AbstractCard card, boolean isEndTurn);
}
