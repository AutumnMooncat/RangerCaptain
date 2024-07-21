package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class GlowChangePatch {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class GlowCheckField {
        public static SpireField<AbstractCard> lastChecked = new SpireField<>(()-> null);
    }
}
