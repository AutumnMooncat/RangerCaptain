package RangerCaptain.util;

import RangerCaptain.TheRangerCaptain;
import RangerCaptain.potions.*;
import basemod.BaseMod;

public class PotionLoader {
    public static void loadContent() {
        BaseMod.addPotion(FizzyPotion.class, FizzyPotion.LIQUID, FizzyPotion.HYBRID, FizzyPotion.SPOTS, FizzyPotion.POTION_ID, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
        BaseMod.addPotion(FissionPotion.class, FissionPotion.LIQUID, FissionPotion.HYBRID, FissionPotion.SPOTS, FissionPotion.POTION_ID, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
        BaseMod.addPotion(SunPotion.class, SunPotion.LIQUID, SunPotion.HYBRID, SunPotion.SPOTS, SunPotion.POTION_ID, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
        BaseMod.addPotion(VigorPotion.class, VigorPotion.LIQUID, VigorPotion.HYBRID, VigorPotion.SPOTS, VigorPotion.POTION_ID, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
        BaseMod.addPotion(ResonancePotion.class, ResonancePotion.LIQUID, ResonancePotion.HYBRID, ResonancePotion.SPOTS, ResonancePotion.POTION_ID, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
    }
}
