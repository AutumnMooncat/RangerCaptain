package RangerCaptain.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Field;

public class ProfessorHelper {
    public static void tagFire(AbstractCard card) {
        applyTag(card, "PROF_FIRE");
    }

    public static void tagIce(AbstractCard card) {
        applyTag(card, "PROF_ICE");
    }

    public static void tagBolt(AbstractCard card) {
        applyTag(card, "PROF_BOLT");
    }

    public static void tagWind(AbstractCard card) {
        applyTag(card, "PROF_WIND");
    }

    public static void tagNotFire(AbstractCard card) {
        applyTag(card, "PROF_NOT_FIRE");
    }

    public static void tagNotIce(AbstractCard card) {
        applyTag(card, "PROF_NOT_ICE");
    }

    public static void tagNotBolt(AbstractCard card) {
        applyTag(card, "PROF_NOT_BOLT");
    }

    public static void tagNotWind(AbstractCard card) {
        applyTag(card, "PROF_NOT_WIND");
    }

    private static void applyTag(AbstractCard card, String tagName) {
        if (Loader.isModLoaded("Professor")) {
            try {
                Class<?> profTags = Class.forName("Professor.patches.CustomTags");
                Field tagField = profTags.getDeclaredField(tagName);
                AbstractCard.CardTags tag = (AbstractCard.CardTags) tagField.get(null);
                card.tags.add(tag);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
