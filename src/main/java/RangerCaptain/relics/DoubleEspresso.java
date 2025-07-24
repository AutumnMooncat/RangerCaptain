package RangerCaptain.relics;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.relics.interfaces.OnStashRelic;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static RangerCaptain.MainModfile.makeID;

public class DoubleEspresso extends AbstractEasyRelic implements OnStashRelic {
    public static final String ID = makeID(DoubleEspresso.class.getSimpleName());
    HashMap<String, Integer> stats = new HashMap<>();
    private final String STAT = DESCRIPTIONS[1];
    private final String PER_TURN = DESCRIPTIONS[2];
    private final String PER_COMBAT = DESCRIPTIONS[3];

    public DoubleEspresso() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR);
        resetStats();
    }

    public void atPreBattle() {
        grayscale = false;
        counter = 3;
    }

    @Override
    public void onStash(AbstractCard card, boolean isEndTurn) {
        if (counter > 0) {
            counter--;
            if (counter == 0) {
                counter = -1;
                grayscale = true;
            }
            flash();
            CardModifierManager.addModifier(card, new FreeCostTrackerMod());
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override //Should replace default relic.
    public void obtain() {
        //Grab the player
        AbstractPlayer p = AbstractDungeon.player;
        //If we have the starter relic...
        if (p.hasRelic(EspressoExpress.ID)) {
            //Grab its data for relic stats if you want to carry the stats over to the boss relic
            EspressoExpress old = (EspressoExpress) p.getRelic(EspressoExpress.ID);
            stats.put(STAT, old.getStat());
            //Find it...
            for (int i = 0; i < p.relics.size(); ++i) {
                if (p.relics.get(i).relicId.equals(EspressoExpress.ID)) {
                    //Replace it
                    instantObtain(p, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    //Only spawn if we have the starter relic
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(EspressoExpress.ID);
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
        float stat = (float)stats.get(STAT);

        // Relic Stats truncates these extended stats to 3 decimal places, so we do the same
        DecimalFormat perTurnFormat = new DecimalFormat("#.###");
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
        DoubleEspresso newRelic = new DoubleEspresso();
        newRelic.stats = this.stats;
        return newRelic;
    }

    private static class FreeCostTrackerMod extends AbstractCardModifier {

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
            AbstractRelic r = Wiz.adp().getRelic(DoubleEspresso.ID);
            if (r instanceof DoubleEspresso) {
                if (card.costForTurn >= 0) {
                    ((DoubleEspresso) r).incrementStat(card.costForTurn);
                } else if (card.cost == -1) {
                    ((DoubleEspresso) r).incrementStat(card.energyOnUse);
                }
            }
        }

        @Override
        public AbstractCardModifier makeCopy() {
            return new FreeCostTrackerMod();
        }
    }
}
