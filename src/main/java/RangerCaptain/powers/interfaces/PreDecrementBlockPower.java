package RangerCaptain.powers.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface PreDecrementBlockPower {
    int preBlockCalc(AbstractMonster m, DamageInfo info, int damageAmount);
}
