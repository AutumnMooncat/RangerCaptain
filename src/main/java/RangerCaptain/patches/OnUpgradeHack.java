package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.ApotheosisAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class OnUpgradeHack {
    @SpirePatch2(clz = AbstractPlayer.class, method = "bottledCardUpgradeCheck")
    public static class PermanentUpgrade {
        @SpirePostfixPatch
        public static void plz(AbstractCard c) {
            MainModfile.onUpgradeTrigger(c, true);
        }
    }

    @SpirePatch2(clz = UpgradeSpecificCardAction.class, method = "update")
    public static class TempUpgrade1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(AbstractCard ___c) {
            MainModfile.onUpgradeTrigger(___c, false);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "superFlash");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = UpgradeRandomCardAction.class, method = "update")
    public static class TempUpgrade2 {
        @SpireInsertPatch(locator = Locator.class, localvars = {"upgradeable"})
        public static void plz(CardGroup upgradeable) {
            MainModfile.onUpgradeTrigger(upgradeable.group.get(0), false);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "superFlash");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = ArmamentsAction.class, method = "update")
    public static class TempUpgrade3 {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz() {
            MainModfile.onUpgradeTrigger(AbstractDungeon.player.hand.getTopCard(), false);
        }

        @SpireInsertPatch(locator = Locator2.class, localvars = {"c"})
        public static void plz2(AbstractCard c) {
            MainModfile.onUpgradeTrigger(c, false);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "superFlash");
                return new int[] {LineFinder.findAllInOrder(ctBehavior, m)[2]};
            }
        }

        public static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "superFlash");
                int[] matches = LineFinder.findAllInOrder(ctBehavior, m);
                return new int[] {matches[0], matches[1], matches[3]};
            }
        }
    }

    @SpirePatch2(clz = ApotheosisAction.class, method = "upgradeAllCardsInGroup")
    public static class TempUpgrade4 {
        @SpireInsertPatch(locator = Locator.class, localvars = {"c"})
        public static void plz(AbstractCard c) {
            MainModfile.onUpgradeTrigger(c, false);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "applyPowers");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
