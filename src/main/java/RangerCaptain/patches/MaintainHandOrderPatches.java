/*
package CelestialHostess.patches;

import CelestialHostess.TheCelestialHostess;
import CelestialHostess.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MaintainHandOrderPatches {
    private static final ArrayList<AbstractCard> cardOrder = new ArrayList<>();
    @SpirePatch2(clz = HandCardSelectScreen.class, method = "prep")
    public static class SaveOrder {
        @SpirePostfixPatch
        public static void save() {
            if (Wiz.adp() instanceof TheCelestialHostess) {
                cardOrder.clear();
                cardOrder.addAll(Wiz.adp().hand.group);
            }
        }
    }

    @SpirePatch2(clz = HandCardSelectScreen.class, method = "refreshSelectedCards")
    public static class LoadOrder {
        @SpirePostfixPatch
        public static void load() {
            if (Wiz.adp() instanceof TheCelestialHostess) {
                ArrayList<AbstractCard> newCards = cardOrder.stream().filter(Wiz.adp().hand::contains).collect(Collectors.toCollection(ArrayList::new));
                Wiz.adp().hand.group.removeAll(newCards);
                Wiz.adp().hand.group.addAll(newCards);
                Wiz.adp().hand.refreshHandLayout();
            }
        }
    }
}
*/
