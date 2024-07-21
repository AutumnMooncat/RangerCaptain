package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnCounterPower {
    void onCounter(AbstractCreature target, int damage);
}
