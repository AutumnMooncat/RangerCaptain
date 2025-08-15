package RangerCaptain.relics;

import RangerCaptain.MainModfile;
import RangerCaptain.TheRangerCaptain;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.relics.CardRewardSkipButtonRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class Upgrape extends AbstractEasyRelic implements CardRewardSkipButtonRelic {
    public static final String ID = makeID(Upgrape.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String SKIP_TEXT = DESCRIPTIONS[1];
    private final String STAT = DESCRIPTIONS[2];

    public Upgrape() {
        super(ID, RelicTier.RARE, LandingSound.FLAT, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
        counter = 0;
    }

    @Override
    public void onPreviewObtainCard(AbstractCard c) {
        // This is extremely jank
        MainModfile.safeEffectQueue.add(new AbstractGameEffect() {
            @Override
            public void update() {
                this.isDone = true;
                if (c.canUpgrade() && counter > 0 && !AlreadyUpgradedField.alreadyUpgraded.get(c)) {
                    counter--;
                    c.upgrade();
                    flash();
                    // RewardItems will call this hook twice for each card, only use charges once
                    AlreadyUpgradedField.alreadyUpgraded.set(c, true);
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {}

            @Override
            public void dispose() {}
        });
    }

    @Override
    public void onClickedButton() {
        flash();
        incrementStat(1);
        counter += 2;
    }

    @Override
    public String getButtonLabel() {
        return SKIP_TEXT;
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
        return getStatsDescription();
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
        Upgrape newRelic = new Upgrape();
        newRelic.stats = this.stats;
        return newRelic;
    }

    @SpirePatch2(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class AlreadyUpgradedField {
        public static SpireField<Boolean> alreadyUpgraded = new SpireField<>(() -> false);
    }
}
