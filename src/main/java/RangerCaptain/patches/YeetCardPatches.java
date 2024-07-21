package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class YeetCardPatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class YeetField {
        public static SpireField<Boolean> yeet = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            YeetField.yeet.set(result, YeetField.yeet.get(self));
            return result;
        }
    }

    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class YeetCard {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> yeet(UseCardAction __instance, AbstractCard ___targetCard) {
            if (YeetField.yeet.get(___targetCard)) {
                __instance.isDone = true;
                AbstractDungeon.player.cardInUse = null;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m =  new Matcher.FieldAccessMatcher(AbstractCard.class, "purgeOnUse");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
