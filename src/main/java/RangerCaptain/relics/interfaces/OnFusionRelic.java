package RangerCaptain.relics.interfaces;

import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnFusionRelic {
    void onPerformFusion(AbstractCard base, AbstractCard donor, FusedCard fused);
}
