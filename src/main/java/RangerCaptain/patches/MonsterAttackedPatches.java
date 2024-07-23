package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MonsterAttackedPatches {
    @SpirePatch2(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class AttackCountField {
        public static SpireField<Integer> attackedCounter = new SpireField<>(() -> 0);
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class OnDamaged {
        @SpirePrefixPatch
        public static void plz (AbstractMonster __instance, DamageInfo info) {
            if (info.type == DamageInfo.DamageType.NORMAL) {
                AttackCountField.attackedCounter.set(__instance, AttackCountField.attackedCounter.get(__instance)+1);
            }
        }
    }
}
