package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.relics.PrizeTicket;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.OnSaleTag;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class ShopPatches {
    @SpirePatch2(clz = OnSaleTag.class, method = SpirePatch.CLASS)
    public static class OriginalCostField {
        public static SpireField<Integer> originalCost = new SpireField<>(() -> 0);
    }

    @SpirePatch2(clz = ShopScreen.class, method = SpirePatch.CLASS)
    public static class SecondSaleField {
        public static SpireField<OnSaleTag> secondTag = new SpireField<>(() -> null);
    }

    public static void generateSecondSale(ShopScreen screen) {
        OnSaleTag origTag = ReflectionHacks.getPrivate(screen, ShopScreen.class, "saleTag");
        if (origTag != null) {
            OriginalCostField.originalCost.set(origTag, origTag.card.price);
            origTag.card.price = 0;
        }

        ArrayList<AbstractCard> cards = new ArrayList<>(screen.colorlessCards);
        cards.addAll(screen.coloredCards);
        cards.removeIf(c -> origTag != null && origTag.card == c);
        if (cards.stream().anyMatch(c -> c.price > 0)) {
            // Dont pick a free card if possible
            cards.removeIf(c -> c.price == 0);
        }
        if (!cards.isEmpty()) {
            AbstractCard newSaleCard = cards.get(AbstractDungeon.merchantRng.random(cards.size() - 1));
            OnSaleTag newTag = new OnSaleTag(newSaleCard);
            OriginalCostField.originalCost.set(newTag, newSaleCard.price);
            newSaleCard.price /= 2;
            SecondSaleField.secondTag.set(screen, newTag);
        }
    }

    @SpirePatch2(clz = ShopScreen.class, method = "initCards")
    public static class Tag2ElectricBoogaloo {
        @SpirePostfixPatch
        public static void makeTag(ShopScreen __instance) {
            if (AbstractDungeon.player.hasRelic(PrizeTicket.ID)) {
                generateSecondSale(__instance);
            }
        }
    }

    public static boolean checkSecondTag(ShopScreen screen, Object card) {
        OnSaleTag tag2 = SecondSaleField.secondTag.get(screen);
        return tag2 != null && tag2.card == card;
    }

    @SpirePatch2(clz = ShopScreen.class, method = "renderCardsAndPrices")
    public static class RenderSecondTag {
        @SpireInstrumentPatch
        public static ExprEditor patch() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("equals")) {
                        m.replace(String.format("$_ = $proceed($$) || %s.checkSecondTag(this, $0);", ShopPatches.class.getName()));
                    }
                }
            };
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void plz(ShopScreen __instance, SpriteBatch sb) {
            OnSaleTag tag2 = SecondSaleField.secondTag.get(__instance);
            if (tag2 != null) {
                if (__instance.coloredCards.contains(tag2.card) || __instance.colorlessCards.contains(tag2.card)) {
                    tag2.render(sb);
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(ShopScreen.class, "coloredCards");
                return new int[] {LineFinder.findAllInOrder(ctBehavior, m)[1]};
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "loseGold")
    public static class FreeIsFree {
        @SpirePrefixPatch
        public static SpireReturn<Void> plz(int goldAmount) {
            if (AbstractDungeon.getCurrRoom() instanceof ShopRoom && goldAmount <= 0 && MainModfile.freeIsFree) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = ShopScreen.class, method = "purchaseCard")
    public static class TrackSale {
        @SpireInsertPatch(locator = Locator.class)
        public static void cardGet(ShopScreen __instance, AbstractCard hoveredCard) {
            PrizeTicket ticket = (PrizeTicket) AbstractDungeon.player.getRelic(PrizeTicket.ID);
            if (ticket != null) {
                OnSaleTag tag = ReflectionHacks.getPrivate(__instance, ShopScreen.class, "saleTag");
                OnSaleTag tag2 = SecondSaleField.secondTag.get(__instance);
                if (tag != null && tag.card == hoveredCard) {
                    ticket.incrementCards(1);
                    ticket.incrementSavings(OriginalCostField.originalCost.get(tag) - hoveredCard.price);
                } else if (tag2 != null && tag2.card == hoveredCard) {
                    ticket.incrementCards(1);
                    ticket.incrementSavings(OriginalCostField.originalCost.get(tag2) - hoveredCard.price);
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
