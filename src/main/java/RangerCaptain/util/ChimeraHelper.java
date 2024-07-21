package RangerCaptain.util;

import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;
import CardAugments.patches.InfiniteUpgradesPatches;
import RangerCaptain.MainModfile;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ChimeraHelper {
    public static void rollOnSynthesisCard(AbstractCard card) {
        if (CardAugmentsMod.modifyInCombat) {
            CardAugmentsMod.rollCardAugment(card);
        }
    }

    public static boolean hasSearing(AbstractCard card) {
        return InfiniteUpgradesPatches.InfUpgradeField.inf.get(card);
    }

    public static void registerAugments() {
        CardAugmentsMod.registerMod(MainModfile.modID, CardCrawlGame.languagePack.getUIString(MainModfile.makeID("CrossOver")).TEXT[0]);
        new AutoAdd(MainModfile.modID)
                .packageFilter("CelestialHostess.augments")
                .any(AbstractAugment.class, (info, abstractAugment) -> {
                    CardAugmentsMod.registerAugment(abstractAugment, MainModfile.modID);});
    }

    public static void applyBans() {
        //CardAugmentsMod.registerCustomBan(AberrantMod.ID, c -> c instanceof EternalFire);
    }
}
