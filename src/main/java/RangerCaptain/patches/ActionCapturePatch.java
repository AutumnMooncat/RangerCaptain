package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.List;

public class ActionCapturePatch {
    public static boolean doCapture;
    public static List<AbstractGameAction> capturedActions = new ArrayList<>();

    @SpirePatch2(clz = GameActionManager.class, method = "addToBottom")
    @SpirePatch2(clz = GameActionManager.class, method = "addToTop")
    public static class Capture {
        @SpirePrefixPatch
        public static SpireReturn<Void> yoink(AbstractGameAction action) {
            if (doCapture) {
                capturedActions.add(action);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    public static void releaseToBot() {
        doCapture = false;
        for (AbstractGameAction action : capturedActions) {
            AbstractDungeon.actionManager.addToBottom(action);
        }
        capturedActions.clear();
    }

    public static void releaseToTop() {
        doCapture = false;
        for (int i = capturedActions.size() - 1; i >= 0; i--) {
            AbstractDungeon.actionManager.addToTop(capturedActions.get(i));
        }
        capturedActions.clear();
    }
}
