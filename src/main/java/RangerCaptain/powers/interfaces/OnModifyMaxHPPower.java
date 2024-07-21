package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;

public interface OnModifyMaxHPPower {
    int onModifyMaxHP(AbstractCreature target, int amount);
}
