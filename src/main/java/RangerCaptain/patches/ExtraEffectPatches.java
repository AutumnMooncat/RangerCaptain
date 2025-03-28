package RangerCaptain.patches;

import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.ui.DiscoveredCardManager;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;

public class ExtraEffectPatches {
    private static boolean yeetCard = false;

    @SpirePatch(clz = AbstractCard.class, method = "<class>")
    public static class EffectFields {
        public static SpireField<Integer> mindMeldCount = new SpireField<>(() -> 0);
        public static SpireField<Boolean> closeEncounter = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        @SpirePostfixPatch
        public static AbstractCard plz(AbstractCard result, AbstractCard self) {
            EffectFields.mindMeldCount.set(result, EffectFields.mindMeldCount.get(self));
            EffectFields.closeEncounter.set(result, EffectFields.closeEncounter.get(self));
            return result;
        }
    }

    @SpirePatch2(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class DoExtraEffects {
        @SpirePrefixPatch
        public static void plz(UseCardAction __instance, AbstractCard card) {
            int meldCount = EffectFields.mindMeldCount.get(card);
            if (meldCount > 0) {
                AbstractCard copy = card.makeStatEquivalentCopy();
                EffectFields.mindMeldCount.set(copy, 0);
                EffectFields.closeEncounter.set(copy, false);
                for (int i = 0 ; i < meldCount ; i ++) {
                    Wiz.applyToSelf(new MindMeldPower(Wiz.adp(), copy));
                }
            }
            /*if (EffectFields.closeEncounter.get(card)) {
                Wiz.applyToSelf(new CloseEncounterPower(Wiz.adp(), card));
            }*/
        }
    }

    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class FlagForDiscovery {
        @SpireInsertPatch(locator = Locator.class)
        public static void yeetCheck(UseCardAction __instance, AbstractCard ___targetCard) {
            if (EffectFields.closeEncounter.get(___targetCard)) {
                yeetCard = true;
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(CardGroup.class, "moveToDiscardPile");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }

    @SpirePatch2(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class SendToDiscovery {
        @SpirePrefixPatch
        public static SpireReturn<?> yeet(CardGroup __instance, AbstractCard c) {
            if (yeetCard) {
                yeetCard = false;
                DiscoveredCardManager.addCard(c, false);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
