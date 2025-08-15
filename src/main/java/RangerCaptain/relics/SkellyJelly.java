package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class SkellyJelly extends AbstractEasyRelic {
    public static final String ID = makeID(SkellyJelly.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String DRAWN = DESCRIPTIONS[1];
    private final String PLAYED = DESCRIPTIONS[4];
    private final String PER_TURN = DESCRIPTIONS[2];
    private final String PER_COMBAT = DESCRIPTIONS[3];

    public SkellyJelly() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        Slimed slime = new Slimed();
        CardModifierManager.addModifier(slime, new TrackerMod());
        this.addToBot(new MakeTempCardInDrawPileAction(slime, 3, true, true));
    }

    @Override
    public void onEquip() {
        ++AbstractDungeon.player.energy.energyMaster;
    }

    @Override
    public void onUnequip() {
        --AbstractDungeon.player.energy.energyMaster;
    }

    public void incrementDrawn(int amount) {
        stats.put(DRAWN, stats.get(DRAWN) + amount);
    }

    public void incrementPlayed(int amount) {
        stats.put(PLAYED, stats.get(PLAYED) + amount);
    }

    public String getStatsDescription() {
        return DRAWN + stats.get(DRAWN) + PLAYED + stats.get(PLAYED);
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        // You would just return getStatsDescription() if you don't want to display per-combat and per-turn stats
        StringBuilder builder = new StringBuilder();

        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");

        float stat = stats.get(DRAWN);
        builder.append(DRAWN).append(stats.get(DRAWN));
        builder.append(PER_TURN);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));

        stat = stats.get(PLAYED);
        builder.append(PLAYED).append(stats.get(PLAYED));
        builder.append(PER_TURN);
        builder.append(perTurnFormat.format(stat / Math.max(totalTurns, 1)));
        builder.append(PER_COMBAT);
        builder.append(perTurnFormat.format(stat / Math.max(totalCombats, 1)));

        return builder.toString();
    }

    public void resetStats() {
        stats.put(DRAWN, 0);
        stats.put(PLAYED, 0);
    }

    public JsonElement onSaveStats() {
        // An array makes more sense if you want to store more than one stat
        Gson gson = new Gson();
        ArrayList<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(DRAWN));
        statsToSave.add(stats.get(PLAYED));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(DRAWN, jsonArray.get(0).getAsInt());
            stats.put(PLAYED, jsonArray.get(1).getAsInt());
        } else {
            resetStats();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        // Relic Stats will always query the stats from the instance passed to BaseMod.addRelic()
        // Therefore, we make sure all copies share the same stats by copying the HashMap.
        SkellyJelly newRelic = new SkellyJelly();
        newRelic.stats = this.stats;
        return newRelic;
    }

    private static class TrackerMod extends AbstractCardModifier {
        @Override
        public void onDrawn(AbstractCard card) {
            AbstractRelic r = Wiz.adp().getRelic(SkellyJelly.ID);
            if (r instanceof SkellyJelly) {
                ((SkellyJelly) r).incrementDrawn(1);
            }
        }

        @Override
        public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
            AbstractRelic r = Wiz.adp().getRelic(SkellyJelly.ID);
            if (r instanceof SkellyJelly) {
                ((SkellyJelly) r).incrementPlayed(1);
            }
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new TrackerMod();
        }
    }
}
