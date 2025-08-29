package RangerCaptain.patches;

import RangerCaptain.cards.interfaces.OnCreateCardCard;
import RangerCaptain.powers.interfaces.OnCreateCardPower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;

public class OnCreateCardPatches {
    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class AlreadyModifiedField {
        public static SpireField<Boolean> modified = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            AlreadyModifiedField.modified.set(result, AlreadyModifiedField.modified.get(self));
            return result;
        }
    }

    @SpirePatch2(clz = CardRewardScreen.class, method = "customCombatOpen")
    public static class DiscoveryStyleCards {
        @SpirePrefixPatch
        public static void plz(ArrayList<AbstractCard> choices) {
            for (AbstractPower p : Wiz.adp().powers) {
                if (p instanceof OnCreateCardPower) {
                    for (AbstractCard c : choices) {
                        ((OnCreateCardPower) p).onGenerateCardOption(c);
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    @SpirePatch2(clz = ShowCardAndAddToHandEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    @SpirePatch2(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, boolean.class, boolean.class})
    @SpirePatch2(clz = ShowCardAndAddToDrawPileEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class, boolean.class, boolean.class, boolean.class})
    @SpirePatch2(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class})
    @SpirePatch2(clz = ShowCardAndAddToDiscardEffect.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {AbstractCard.class, float.class, float.class})
    public static class CreatedCards {
        @SpirePostfixPatch
        public static void plz(Object[] __args) {
            if (__args[0] instanceof AbstractCard && !AlreadyModifiedField.modified.get(__args[0])) {
                CardCounterPatches.cardsCreatedThisCombat.add((AbstractCard) __args[0]);
                for (AbstractCard card : Wiz.adp().hand.group) {
                    if (card instanceof OnCreateCardCard) {
                        ((OnCreateCardCard) card).onCreateCard((AbstractCard) __args[0]);
                    }
                }
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof OnCreateCardPower) {
                        ((OnCreateCardPower) p).onCreateCard((AbstractCard) __args[0]);
                    }
                }
            }
        }
    }
}
