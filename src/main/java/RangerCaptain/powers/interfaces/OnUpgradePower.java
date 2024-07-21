package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnUpgradePower {
    void onUpgrade(AbstractCard c);
    boolean allowUpgrade(AbstractCard c);
}
