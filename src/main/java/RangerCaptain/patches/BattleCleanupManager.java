package RangerCaptain.patches;

import basemod.BaseMod;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostDeathSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

public class BattleCleanupManager implements StartGameSubscriber, PostBattleSubscriber, PostDeathSubscriber {
    private static final ArrayList<CleanupLogic> logicEntities = new ArrayList<>();

    public BattleCleanupManager() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        BattleCleanupManager manager = new BattleCleanupManager();
    }

    public static CleanupLogic addLogic(Runnable cleanup) {
        CleanupLogic e = new CleanupLogic(cleanup);
        logicEntities.add(e);
        return e;
    }

    public static void runAllCleanup() {
        for (CleanupLogic e : logicEntities) {
            e.processCleanup();
        }
        logicEntities.clear();
    }

    public static void runCleanup(CleanupLogic e) {
        e.processCleanup();
        logicEntities.remove(e);
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        runAllCleanup();
    }

    @Override
    public void receivePostDeath() {
        runAllCleanup();
    }

    @Override
    public void receiveStartGame() {
        runAllCleanup();
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "resetPlayer")
    @SpirePatch2(clz = CardCrawlGame.class, method = "startOver")
    public static class CleanupOnRestart {
        @SpirePostfixPatch
        public static void plz() {
            runAllCleanup();
        }
    }

    public static class CleanupLogic {
        private final Runnable cleanup;

        public CleanupLogic(Runnable cleanup) {
            this.cleanup = cleanup;
        }

        private void processCleanup() {
            cleanup.run();
        }
    }
}
