package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class Rewind extends AbstractEasyRelic {
    public static final String ID = makeID(Rewind.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String ATTACK_STAT = DESCRIPTIONS[1];
    private final String SKILL_STAT = DESCRIPTIONS[2];
    private final String POWER_STAT = DESCRIPTIONS[3];
    private final String STATUS_STAT = DESCRIPTIONS[4];
    private final String CURSE_STAT = DESCRIPTIONS[5];

    public Rewind() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    public void atPreBattle() {
        grayscale = false;
    }

    @Override
    public void onExhaust(AbstractCard card) {
        if (!grayscale) {
            grayscale = true;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToTop(new MakeTempCardInHandAction(card));
            switch (card.type) {
                case ATTACK:
                    incrementStat(ATTACK_STAT, 1);
                    break;
                case SKILL:
                    incrementStat(SKILL_STAT, 1);
                    break;
                case POWER:
                    incrementStat(POWER_STAT, 1);
                    break;
                case STATUS:
                    incrementStat(STATUS_STAT, 1);
                    break;
                case CURSE:
                    incrementStat(CURSE_STAT, 1);
                    break;
            }
        }
    }

    public void incrementStat(String key, int amount) {
        stats.put(key, stats.get(key) + amount);
    }

    public String getStatsDescription() {
        return ATTACK_STAT + stats.get(ATTACK_STAT) + SKILL_STAT + stats.get(SKILL_STAT) + POWER_STAT + stats.get(POWER_STAT);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        return getStatsDescription() + STATUS_STAT + stats.get(STATUS_STAT) + CURSE_STAT + stats.get(CURSE_STAT);
    }

    public void resetStats() {
        stats.put(ATTACK_STAT, 0);
        stats.put(SKILL_STAT, 0);
        stats.put(POWER_STAT, 0);
        stats.put(STATUS_STAT, 0);
        stats.put(CURSE_STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(ATTACK_STAT));
        statsToSave.add(stats.get(SKILL_STAT));
        statsToSave.add(stats.get(POWER_STAT));
        statsToSave.add(stats.get(STATUS_STAT));
        statsToSave.add(stats.get(CURSE_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(ATTACK_STAT, jsonArray.get(0).getAsInt());
            stats.put(SKILL_STAT, jsonArray.get(1).getAsInt());
            stats.put(POWER_STAT, jsonArray.get(2).getAsInt());
            stats.put(STATUS_STAT, jsonArray.get(3).getAsInt());
            stats.put(CURSE_STAT, jsonArray.get(4).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        Rewind newRelic = new Rewind();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
