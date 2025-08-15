package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import static RangerCaptain.MainModfile.makeID;

public class OpticalLaserTape extends AbstractEasyRelic implements CustomBottleRelic, CustomSavable<String> {
    public static final String ID = makeID(OpticalLaserTape.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String INSERTED_NAME_TEXT = DESCRIPTIONS[1];
    private final String SELECT_TEXT = DESCRIPTIONS[2];
    private final String ENERGY_SAVED = DESCRIPTIONS[3];
    private final String PER_TURN = DESCRIPTIONS[4];
    private final String PER_COMBAT = DESCRIPTIONS[5];

    private boolean cardsSelected = true;

    public OpticalLaserTape() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.masterDeck.group.stream().anyMatch(Wiz::canBeFused);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        for (AbstractCard card : Wiz.getAllCardsInCardGroups(true, true)) {
            if (isOnCard().test(StSLib.getMasterDeckEquivalent(card))) {
                CardModifierManager.addModifier(card, new FreeToPlayTracker());
            }
        }
    }

    @Override
    public void onEquip() {
        cardsSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (Wiz.canBeFused(c)) {
                tmp.addToTop(c);
            }
        }
        if (tmp.group.isEmpty()) {
            cardsSelected = true;
        } else {
            AbstractDungeon.gridSelectScreen.open(tmp, 1, SELECT_TEXT, false, false, false, false);
        }
    }

    @Override
    public void onUnequip() {
        AbstractCard card = findMasterCard();
        if (card != null) {
            CardModifierManager.removeModifiersById(card, BottleTracker.ID, true);
            CardModifierManager.removeModifiersById(card, FreeToPlayTracker.ID, true);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardsSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            CardModifierManager.addModifier(c, new BottleTracker());
            setNameInDescription(c.name);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            cardsSelected = true;
            (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return c -> CardModifierManager.modifiers(c).stream().anyMatch(mod -> mod instanceof BottleTracker);
    }

    @Override
    public String onSave() {
        AbstractCard card = findMasterCard();
        if (card != null) {
            return card.name;
        }
        return null;
    }

    @Override
    public void onLoad(String s) {
        if (s != null) {
            setNameInDescription(s);
        }
    }

    public void setNameInDescription(String cardName) {
        description = String.format(INSERTED_NAME_TEXT, FontHelper.colorString(cardName, "y"));
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    public AbstractCard findMasterCard() {
        if (Wiz.adp() != null) {
            for (AbstractCard card : Wiz.adp().masterDeck.group) {
                if (isOnCard().test(card)) {
                    return card;
                }
            }
        }
        return null;
    }

    public void incrementStat(int amount) {
        stats.put(ENERGY_SAVED, stats.get(ENERGY_SAVED) + amount);
    }

    public String getStatsDescription() {
        return ENERGY_SAVED + stats.get(ENERGY_SAVED);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());

        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");

        float stat = stats.get(ENERGY_SAVED);
        builder.append(PER_TURN);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));

        return builder.toString();
    }

    public void resetStats() {
        stats.put(ENERGY_SAVED, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(ENERGY_SAVED));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(ENERGY_SAVED, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        OpticalLaserTape newRelic = new OpticalLaserTape();
        newRelic.stats = this.stats;
        return newRelic;
    }

    private static class BottleTracker extends AbstractCardModifier {
        public static String ID = OpticalLaserTape.ID+BottleTracker.class.getSimpleName();

        @Override
        public boolean isInherent(AbstractCard card) {
            return true;
        }

        @Override
        public String identifier(AbstractCard card) {
            return ID;
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new BottleTracker();
        }
    }

    private static class FreeToPlayTracker extends AbstractCardModifier {
        public static String ID = OpticalLaserTape.ID+FreeToPlayTracker.class.getSimpleName();

        @Override
        public void onInitialApplication(AbstractCard card) {
            card.freeToPlayOnce = true;
        }

        @Override
        public boolean removeOnCardPlayed(AbstractCard card) {
            return true;
        }

        @Override
        public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
            AbstractRelic r = Wiz.adp().getRelic(OpticalLaserTape.ID);
            if (r instanceof OpticalLaserTape) {
                if (card.costForTurn >= 0) {
                    ((OpticalLaserTape) r).incrementStat(card.costForTurn);
                } else if (card.cost == -1) {
                    ((OpticalLaserTape) r).incrementStat(card.energyOnUse);
                }
            }
        }

        @Override
        public String identifier(AbstractCard card) {
            return ID;
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new FreeToPlayTracker();
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class, method = "bottledCardUpgradeCheck")
    public static class UpdateName {
        @SpirePostfixPatch
        public static void plz(AbstractCard c) {
            AbstractRelic tape = Wiz.adp().getRelic(OpticalLaserTape.ID);
            if (tape instanceof OpticalLaserTape && ((OpticalLaserTape) tape).isOnCard().test(c)) {
                ((OpticalLaserTape) tape).setNameInDescription(c.name);
            }
        }
    }
}
