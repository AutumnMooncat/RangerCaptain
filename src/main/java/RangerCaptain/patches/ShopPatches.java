package RangerCaptain.patches;

import RangerCaptain.relics.PrizeTicket;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.shop.OnSaleTag;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CtBehavior;

public class ShopPatches {
    @SpirePatch2(clz = OnSaleTag.class, method = SpirePatch.CLASS)
    public static class OriginalCostField {
        public static SpireField<Integer> originalCost = new SpireField<>(() -> 0);
    }
    @SpirePatch2(clz = OnSaleTag.class, method = SpirePatch.CONSTRUCTOR)
    public static class FreePlz {
        @SpirePostfixPatch
        public static void stonks(OnSaleTag __instance, AbstractCard c) {
            if (AbstractDungeon.player.hasRelic(PrizeTicket.ID) && c.price > 0) {
                OriginalCostField.originalCost.set(__instance, c.price);
                c.price = 0;
            }
        }
    }
    @SpirePatch2(clz = ShopScreen.class, method = "purchaseCard")
    public static class TrackSale {
        @SpireInsertPatch(locator = Locator.class)
        public static void cardGet(ShopScreen __instance, AbstractCard hoveredCard) {
            OnSaleTag tag = ReflectionHacks.getPrivate(__instance, ShopScreen.class, "saleTag");
            if (tag != null && tag.card == hoveredCard) {
                PrizeTicket ticket = (PrizeTicket) AbstractDungeon.player.getRelic(PrizeTicket.ID);
                if (ticket != null) {
                    ticket.incrementCards(1);
                    ticket.incrementSavings(OriginalCostField.originalCost.get(tag));
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractPlayer.class, "loseGold");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
