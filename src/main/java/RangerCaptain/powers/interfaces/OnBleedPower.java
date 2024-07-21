package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnBleedPower {
    void onBleed(AbstractCreature target, int damage);
}
