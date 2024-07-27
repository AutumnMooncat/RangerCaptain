package RangerCaptain.patches;

import RangerCaptain.cards.WarpSickness;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

public class NoCurseInPoolPatches {
    @SpirePatch2(clz = AbstractDungeon.class, method = "addCurseCards")
    public static class RemoveFromCursePool {
        @SpirePostfixPatch
        public static void plz(AbstractDungeon __instance) {
            AbstractDungeon.curseCardPool.group.removeIf(card -> card instanceof WarpSickness);
        }
    }

    @SpirePatch2(clz = CardLibrary.class, method = "getCurse", paramtypez = {})
    @SpirePatch2(clz = CardLibrary.class, method = "getCurse", paramtypez = {AbstractCard.class, Random.class})
    public static class RemoveFromGetCurse {
        @SpireInsertPatch(locator = Locator.class, localvars = {"tmp"})
        public static void plz(ArrayList<String> tmp) {
            tmp.removeIf(id -> id.equals(WarpSickness.ID));
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
