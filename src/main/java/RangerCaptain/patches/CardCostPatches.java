package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.CheatCostPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

public class CardCostPatches {
    @SpirePatch2(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class CardCostPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> bePlayable(AbstractCard __instance) {
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof CheatCostPower && ((CheatCostPower) p).canAfford(__instance)) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "useCard")
    public static class ConsumePowerPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void useSnow(AbstractPlayer __instance, AbstractCard c) {
            if (c.costForTurn > EnergyPanel.getCurrentEnergy() && c.costForTurn > 0) {
                for (AbstractPower p : __instance.powers) {
                    if (p instanceof CheatCostPower) {
                        ((CheatCostPower) p).onActivate(c);
                        break;
                    }
                }
            }
        }
        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(EnergyManager.class, "use");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
