package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ActionMarkerPatch {

    @SpirePatch2(clz = AbstractGameAction.class, method = SpirePatch.CLASS)
    public static class ActionFields {
        public static SpireField<ArrayList<Object>> markers = new SpireField<>(ArrayList::new);
    }

    @SpirePatch2(clz = GameActionManager.class, method = "addToBottom")
    @SpirePatch2(clz = GameActionManager.class, method = "addToTop")
    public static class PassMarkers {
        @SpirePostfixPatch
        public static void plz(@ByRef AbstractGameAction[] action) {
            if (AbstractDungeon.actionManager.currentAction != null && action[0] != null) {
                ActionFields.markers.get(action[0]).addAll(ActionFields.markers.get(AbstractDungeon.actionManager.currentAction));
            }
        }
    }
}
