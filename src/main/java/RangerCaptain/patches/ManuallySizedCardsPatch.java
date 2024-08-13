package RangerCaptain.patches;

import RangerCaptain.cards.interfaces.ManuallySizeAdjustedCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.ShrinkLongDescription;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.FontHelper;

public class ManuallySizedCardsPatch {
    @SpirePatch(clz = ShrinkLongDescription.ShrinkInitializeDescription.class, method = "Prefix")
    public static class SetTextSizeAndFontScale {
        @SpirePostfixPatch
        public static void shrink(AbstractCard __instance, int ___depth) {
            if (___depth == 0 && __instance instanceof ManuallySizeAdjustedCard) {
                ShrinkLongDescription.Scale.descriptionScale.set(__instance, ((ManuallySizeAdjustedCard) __instance).getAdjustedScale());
                FontHelper.cardDescFont_N.getData().setScale(((ManuallySizeAdjustedCard) __instance).getAdjustedScale());
            }
        }
    }

    @SpirePatch(clz = ShrinkLongDescription.ShrinkInitializeDescription.class, method = "Postfix")
    public static class RenderAndFixFontScale {
        @SpirePostfixPatch
        public static void restore(AbstractCard __instance, int ___depth) {
            if (___depth == 0 && __instance instanceof ManuallySizeAdjustedCard) {
                FontHelper.cardDescFont_N.getData().setScale(1f);
            }
        }
    }
}
