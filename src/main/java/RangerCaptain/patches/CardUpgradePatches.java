package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.*;
import org.clapper.util.classutil.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CardUpgradePatches {
    public static boolean previewMultipleUpgrade = false;
    public static int upgradeTimes = 0;

    public static void applyUnlock(AbstractCard card) {
        CardUpgradePatches.ForcedUpgradeField.inf.set(card, true);
    }

    @SpirePatch2(clz = HandCardSelectScreen.class, method = "selectHoveredCard")
    @SpirePatch2(clz = HandCardSelectScreen.class, method = "updateMessage")
    public static class ShowMultipleUpgrades {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(HandCardSelectScreen __instance) {
            if (previewMultipleUpgrade) {
                applyUnlock(__instance.upgradePreviewCard);
                for (int i = 0 ; i < upgradeTimes-1 ; i++) {
                    __instance.upgradePreviewCard.upgrade();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = GridCardSelectScreen.class, method = "update")
    public static class ShowMultipleUpgrades2 {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(GridCardSelectScreen __instance) {
            if (previewMultipleUpgrade) {
                applyUnlock(__instance.upgradePreviewCard);
                for (int i = 0 ; i < upgradeTimes-1 ; i++) {
                    __instance.upgradePreviewCard.upgrade();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "upgrade");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class ForcedUpgradeField {
        public static SpireField<Boolean> inf = new SpireField<>(() -> false);
    }

    public static void infCheck(AbstractCard card) {
        if (ForcedUpgradeField.inf.get(card)) {
            card.upgraded = false;
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        @SpireInsertPatch(locator = Locator.class, localvars = {"card"})
        public static void copyField(AbstractCard __instance, AbstractCard card) {
            if (ForcedUpgradeField.inf.get(__instance)) {
                ForcedUpgradeField.inf.set(card, true);
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(AbstractCard.class, "timesUpgraded");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "canUpgrade")
    public static class BypassUpgradeLimit {
        @SpirePrefixPatch
        public static SpireReturn<?> plz(AbstractCard __instance) {
            if (ForcedUpgradeField.inf.get(__instance)) {
                return SpireReturn.Return(true);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = SpirePatch.CONSTRUCTOR)
    public static class AbstractCardDynamicPatch {
        @SpireRawPatch
        public static void patch(CtBehavior ctBehavior) throws NotFoundException {
            ClassFinder finder = new ClassFinder();
            finder.add(new File(Loader.STS_JAR));

            for (ModInfo modInfo : Loader.MODINFOS) {
                if (modInfo.jarURL != null) {
                    try {
                        finder.add(new File(modInfo.jarURL.toURI()));
                    } catch (URISyntaxException ignored) {}
                }
            }

            ClassFilter filter = new AndClassFilter(
                    new NotClassFilter(new InterfaceOnlyClassFilter()),
                    new ClassModifiersClassFilter(Modifier.PUBLIC),
                    new OrClassFilter(
                            new SubclassClassFilter(AbstractCard.class),
                            (classInfo, classFinder) -> classInfo.getClassName().equals(AbstractCard.class.getName())
                    )
            );

            ArrayList<ClassInfo> foundClasses = new ArrayList<>();
            finder.findClasses(foundClasses, filter);

            for (ClassInfo classInfo : foundClasses) {
                CtClass ctClass = ctBehavior.getDeclaringClass().getClassPool().get(classInfo.getClassName());
                try {
                    CtMethod[] methods = ctClass.getDeclaredMethods();
                    for (CtMethod m : methods) {
                        if (m.getName().equals("upgrade")) {
                            m.insertBefore(CardUpgradePatches.class.getName() + ".infCheck($0);");
                        }
                    }
                } catch (CannotCompileException ignored) {}
            }
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "upgradeName")
    public static class FixStackOfPlusSymbols {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(AbstractCard __instance) {
            if (ForcedUpgradeField.inf.get(__instance)) {
                __instance.name = __instance.originalName + "+" + __instance.timesUpgraded;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractCard.class, "initializeTitle");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
