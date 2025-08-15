package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.relics.interfaces.OnStashRelic;
import RangerCaptain.util.Wiz;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class EspressoExpress extends AbstractEasyRelic implements OnStashRelic {
    public static final String ID = makeID(EspressoExpress.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String STAT = DESCRIPTIONS[1];
    private final String PER_TURN = DESCRIPTIONS[2];
    private final String PER_COMBAT = DESCRIPTIONS[3];

    public EspressoExpress() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    @Override
    public void atPreBattle() {
        grayscale = false;
    }

    @Override
    public void onStash(AbstractCard card, boolean isEndTurn) {
        if (!grayscale) {
            grayscale = true;
            flash();
            incrementStat(1);
            Wiz.applyToSelfTop(new APBoostPower(AbstractDungeon.player, 1));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    public int getStat() {
        return stats.get(STAT);
    }

    public void incrementStat(int amount) {
        stats.put(STAT, stats.get(STAT) + amount);
    }

    public String getStatsDescription() {
        int stat = stats.get(STAT);
        return STAT + stat;
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();
        builder.append(getStatsDescription());

        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");

        float stat = (float)stats.get(STAT);
        builder.append(PER_TURN);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));

        return builder.toString();
    }

    public void resetStats() {
        stats.put(STAT, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        EspressoExpress newRelic = new EspressoExpress();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
