package RangerCaptain.patches;

import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class MindMeldPatches {
    @SpirePatch(clz = AbstractCard.class, method = "<class>")
    public static class MindMeldField {
        public static SpireField<Integer> mindMeldCount = new SpireField<>(() -> 0);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        @SpirePostfixPatch
        public static AbstractCard plz(AbstractCard result, AbstractCard self) {
            MindMeldField.mindMeldCount.set(result, MindMeldField.mindMeldCount.get(self));
            return result;
        }
    }

    @SpirePatch2(clz = UseCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, AbstractCreature.class})
    public static class DoMindMeldEffects {
        @SpirePrefixPatch
        public static void plz(UseCardAction __instance, AbstractCard card) {
            int meldCount = MindMeldField.mindMeldCount.get(card);
            if (meldCount > 0) {
                AbstractCard copy = card.makeStatEquivalentCopy();
                MindMeldField.mindMeldCount.set(copy, 0);
                for (int i = 0 ; i < meldCount ; i ++) {
                    Wiz.applyToSelf(new MindMeldPower(Wiz.adp(), copy));
                }
            }
        }
    }
}
