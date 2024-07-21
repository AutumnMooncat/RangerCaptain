package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.AbstractCard;

public interface InfusionBoostingPower {
    int damageBoost(AbstractCard card);
    int blockBoost(AbstractCard card);
    int magicBoost(AbstractCard card);
}
