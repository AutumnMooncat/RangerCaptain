package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCreateCardPower {
    void onCreateCard(AbstractCard card);
    void onGenerateCardOption(AbstractCard card);
}
