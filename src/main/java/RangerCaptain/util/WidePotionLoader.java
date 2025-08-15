package RangerCaptain.util;

import RangerCaptain.potions.*;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;

public class WidePotionLoader {
    public static void loadCrossoverContent() {
        WidePotionsMod.whitelistSimplePotion(FizzyPotion.POTION_ID);
        WidePotionsMod.whitelistSimplePotion(FissionPotion.POTION_ID);
        WidePotionsMod.whitelistSimplePotion(SunPotion.POTION_ID);
        WidePotionsMod.whitelistSimplePotion(VigorPotion.POTION_ID);
        WidePotionsMod.whitelistSimplePotion(ResonancePotion.POTION_ID);
    }
}
