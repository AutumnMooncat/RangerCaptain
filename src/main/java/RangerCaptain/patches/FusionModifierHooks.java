package RangerCaptain.patches;

import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.CardModifierPatches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class FusionModifierHooks {
    public static void onUpgrade(AbstractCard card) {
        for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
            if (mod instanceof AbstractFusionMod) {
                ((AbstractFusionMod) mod).onUpgrade(card);
            }
        }
    }

    public static float onModifyBaseSecondMagic(float magic, AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
                if (mod instanceof AbstractFusionMod) {
                    magic = ((AbstractFusionMod) mod).modifyBaseSecondMagic(magic, card);
                }
            }
        }
        return magic;
    }

    public static float onModifyBaseThirdMagic(float magic, AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
                if (mod instanceof AbstractFusionMod) {
                    magic = ((AbstractFusionMod) mod).modifyBaseThirdMagic(magic, card);
                }
            }
        }
        return magic;
    }

    @SpirePatch2(clz = CardModifierPatches.CardModifierCalculateCardDamage.class, method = "Prefix")
    @SpirePatch2(clz = CardModifierPatches.CardModifierOnApplyPowers.class, method = "Prefix")
    public static class SecondAndThirdMagicManipulation {
        @SpirePostfixPatch
        public static void plz(Object[] __args) {
            if (__args[0] instanceof AbstractEasyCard) {
                AbstractEasyCard card = (AbstractEasyCard) __args[0];
                int m2 = (int) onModifyBaseSecondMagic(card.baseSecondMagic, card);
                if (m2 != card.baseSecondMagic) {
                    card.secondMagic = m2;
                    card.isSecondMagicModified = true;
                }

                int m3 = (int) onModifyBaseThirdMagic(card.baseThirdMagic, card);
                if (m3 != card.baseSecondMagic) {
                    card.thirdMagic = m3;
                    card.isThirdMagicModified = true;
                }
            }
        }
    }
}
