package RangerCaptain.patches;

import RangerCaptain.powers.interfaces.MonsterOnExhaustPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class ExhaustPatches {
    @SpirePatch2(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class WorkForMonsters {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(CardGroup __instance, AbstractCard c) {
            Wiz.forAllMonstersLiving(mon -> {
                for (AbstractPower pow : mon.powers) {
                    if (pow instanceof MonsterOnExhaustPower) {
                        ((MonsterOnExhaustPower) pow).monsterOnExhaust(c);
                    }
                }
            });
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractCard.class, "triggerOnExhaust");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
