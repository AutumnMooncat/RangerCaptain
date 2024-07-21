package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

public class DelayedExhaustPatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class DelayedExhaustField {
        public static SpireField<Boolean> delayedExhaust = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class CheckDelayedExhaust {
        @SpireInsertPatch(locator = Locator.class)
        public static void check(UseCardAction __instance, AbstractCard ___targetCard) {
            if (DelayedExhaustField.delayedExhaust.get(___targetCard)) {
                __instance.exhaustCard = true;
                DelayedExhaustField.delayedExhaust.set(___targetCard, false);
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
