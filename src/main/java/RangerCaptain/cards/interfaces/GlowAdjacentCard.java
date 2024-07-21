package RangerCaptain.cards.interfaces;

import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface GlowAdjacentCard {
    default boolean glowAdjacent(AbstractCard card) {
        if (this instanceof AbstractCard) {
            return Wiz.getAdjacentCards((AbstractCard) this).contains(card);
        }
        return false;
    }

    default boolean flashAdjacent(AbstractCard card) {
        return glowAdjacent(card);
    }

    Color getGlowColor(AbstractCard card);
}
