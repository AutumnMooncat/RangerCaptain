package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;

public class DontExhaustOnUsePatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class DontExhaustField{
        public static SpireField<Boolean> dontExhaustOnUseOnce = new SpireField<>(() -> false);
    }

    @SpirePatch2(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class DontExhaust {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(UseCardAction __instance, AbstractCard card) {
            if (DontExhaustField.dontExhaustOnUseOnce.get(card)) {
                __instance.exhaustCard = false;
                DontExhaustField.dontExhaustOnUseOnce.set(card, false);
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                return LineFinder.findInOrder(ctBehavior, new Matcher.MethodCallMatcher(UseCardAction.class, "setValues"));
            }
        }
    }
}
