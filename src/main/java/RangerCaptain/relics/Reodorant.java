package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class Reodorant extends AbstractEasyRelic implements OnApplyPowerRelic {
    public static final String ID = makeID(Reodorant.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String STAT = DESCRIPTIONS[1];
    private final String PER_TURN = DESCRIPTIONS[2];
    private final String PER_COMBAT = DESCRIPTIONS[3];

    public Reodorant() {
        super(ID, RelicTier.RARE, LandingSound.CLINK, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        return true;
    }

    @Override
    public int onApplyPowerStacks(AbstractPower power, AbstractCreature target, AbstractCreature source, int stackAmount) {
        if (target instanceof AbstractMonster && power.type == AbstractPower.PowerType.DEBUFF) {
            flash();
            if (power.amount >= 0) {
                stackAmount++;
                power.amount++;
            } else {
                stackAmount--;
                power.amount--;
            }
            power.updateDescription();
            incrementStat(1);
        }
        return stackAmount;
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
        Reodorant newRelic = new Reodorant();
        newRelic.stats = this.stats;
        return newRelic;
    }
}
