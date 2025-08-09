package RangerCaptain.powers.interfaces;

import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnFusionPower {
    void onPerformFusion(AbstractCard base, AbstractCard donor, FusedCard fused);
}
