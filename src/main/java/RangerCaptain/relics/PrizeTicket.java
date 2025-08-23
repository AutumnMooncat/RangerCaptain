package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.patches.ShopPatches;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class PrizeTicket extends AbstractEasyRelic {
    public static final String ID = makeID(PrizeTicket.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String CARDS_STAT = DESCRIPTIONS[1];
    private final String SAVINGS_STAT = DESCRIPTIONS[2];

    public PrizeTicket() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        pulse = room instanceof ShopRoom;
    }

    @Override
    public void onEquip() {
        ShopScreen screen = AbstractDungeon.shopScreen;
        if (screen != null) {
            ShopPatches.generateSecondSale(screen);
        }
    }

    @Override
    public boolean canSpawn() {
        return Settings.isEndless || AbstractDungeon.floorNum <= 48;
    }

    public void incrementCards(int amount) {
        stats.put(CARDS_STAT, stats.get(CARDS_STAT) + amount);
    }

    public void incrementSavings(int amount) {
        stats.put(SAVINGS_STAT, stats.get(SAVINGS_STAT) + amount);
    }

    public String getStatsDescription() {
        return CARDS_STAT + stats.get(CARDS_STAT) + SAVINGS_STAT + stats.get(SAVINGS_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(CARDS_STAT, 0);
        stats.put(SAVINGS_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(CARDS_STAT));
        statsToSave.add(stats.get(SAVINGS_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(CARDS_STAT, jsonArray.get(0).getAsInt());
            stats.put(SAVINGS_STAT, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        PrizeTicket newRelic = new PrizeTicket();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
