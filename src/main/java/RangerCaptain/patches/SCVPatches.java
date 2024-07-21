package RangerCaptain.patches;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.UpgradeChangesPortraitPatch;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;

public class SCVPatches {
    @SpirePatch2(clz = SingleCardViewPopup.class, method = "close")
    @SpirePatch2(clz = SingleCardViewPopup.class, method = "updateBetaArtToggler")
    @SpirePatch2(clz = UpgradeChangesPortraitPatch.ToggleUpgrade.class, method = "Insert")
    public static class DontDispose {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> plz(AbstractCard ___card) {
            if (___card instanceof AbstractEasyCard && ((AbstractEasyCard) ___card).transientArt()) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(Texture.class, "dispose");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
