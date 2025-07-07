package RangerCaptain.patches;

import RangerCaptain.ui.FusionButton;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.EndTurnButton;

public class FusionButtonPatches {
    @SpirePatch2(clz = OverlayMenu.class, method = SpirePatch.CLASS)
    public static class FusionButtonField {
        public static SpireField<FusionButton> button = new SpireField<>(FusionButton::new);
    }

    private static boolean shownThisCombat;

    public static boolean shouldShowButton() {
        return shownThisCombat /*|| Wiz.adp() instanceof TheRangerCaptain*/;
    }

    public static void cardCheck(AbstractCard card) {
        if (Wiz.isInCombat() && Wiz.adp().hand.group.stream().filter(Wiz::canBeFused).count() >= 2) {
            if (AbstractDungeon.overlayMenu != null && FusionButtonField.button.get(AbstractDungeon.overlayMenu) != null) {
                FusionButtonField.button.get(AbstractDungeon.overlayMenu).show();
                shownThisCombat = true;
            }
        }
    }

    @SpirePatch2(clz = GameActionManager.class, method = "clear")
    public static class ResetShownState {
        @SpirePrefixPatch
        public static void reset() {
            shownThisCombat = false;
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "update")
    public static class UpdateAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            FusionButtonField.button.get(__instance).update();
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "render")
    public static class RenderAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance, SpriteBatch sb) {
            FusionButtonField.button.get(__instance).render(sb);
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "showCombatPanels")
    public static class ShowAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            if (shouldShowButton()) {
                FusionButtonField.button.get(__instance).show();
                shownThisCombat = true;
            }
        }
    }

    @SpirePatch2(clz = OverlayMenu.class, method = "hideCombatPanels")
    public static class HideAuraButton {
        @SpirePrefixPatch
        public static void plz(OverlayMenu __instance) {
            FusionButtonField.button.get(__instance).hide();
        }
    }

    @SpirePatch2(clz = EndTurnButton.class, method = "enable")
    public static class EnableAuraButton {
        @SpirePrefixPatch
        public static void plz() {
            FusionButtonField.button.get(AbstractDungeon.overlayMenu).enable();
        }
    }

    @SpirePatch2(clz = EndTurnButton.class, method = "disable", paramtypez = {})
    @SpirePatch2(clz = EndTurnButton.class, method = "disable", paramtypez = {boolean.class})
    public static class DisableAuraButton {
        @SpirePrefixPatch
        public static void plz() {
            FusionButtonField.button.get(AbstractDungeon.overlayMenu).disable();
        }
    }
}
