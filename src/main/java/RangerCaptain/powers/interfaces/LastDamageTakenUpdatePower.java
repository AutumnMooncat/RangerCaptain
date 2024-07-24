package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface LastDamageTakenUpdatePower {
    void onLastDamageTakenUpdate(DamageInfo info, int lastTaken);
}
