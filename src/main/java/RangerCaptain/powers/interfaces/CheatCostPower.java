package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface CheatCostPower {
    boolean canAfford(AbstractCard card);
    void onActivate(AbstractCard card);
}
