package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.Input;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.screens.options.InputSettingsScreen;
import com.megacrit.cardcrawl.screens.options.RemapInputElement;
import javassist.CtBehavior;

import java.util.ArrayList;

public class HotkeyPatches {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("InputSettings")).TEXT;
    public static final String FUSION_HOTKEY_KEY = MainModfile.makeID("FusionHotkey");
    public static final int FUSION_DEFAULT_KEY = Input.Keys.F;
    public static InputAction fusionIA;

    @SpirePatch2(clz = InputActionSet.class, method = "load")
    public static class OnLoad {
        @SpirePrefixPatch
        public static void plzLoad() {
            fusionIA = new InputAction(InputActionSet.prefs.getInteger(FUSION_HOTKEY_KEY, FUSION_DEFAULT_KEY));
        }
    }

    @SpirePatch2(clz = InputActionSet.class, method = "save")
    public static class OnSave {
        @SpirePrefixPatch
        public static void plzSave() {
            InputActionSet.prefs.putInteger(FUSION_HOTKEY_KEY, fusionIA.getKey());
        }
    }

    @SpirePatch2(clz = InputActionSet.class, method = "resetToDefaults")
    public static class OnReset {
        @SpirePrefixPatch
        public static void plzReset() {
            fusionIA.remap(FUSION_DEFAULT_KEY);
        }
    }

    @SpirePatch2(clz = InputSettingsScreen.class, method = "refreshData")
    public static class AddHotkeyOption {
        @SpireInsertPatch(locator = Locator.class)
        public static void plzMake(InputSettingsScreen __instance, ArrayList<RemapInputElement> ___elements) {
            if (!Settings.isControllerMode) {
                ___elements.add(new RemapInputElement(__instance, TEXT[0], fusionIA));
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(InputSettingsScreen.class, "maxScrollAmount");
                return LineFinder.findAllInOrder(ctBehavior, m);
            }
        }
    }
}
