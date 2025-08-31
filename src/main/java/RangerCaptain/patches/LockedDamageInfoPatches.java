package RangerCaptain.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class LockedDamageInfoPatches {
    @SpirePatch2(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class LockedField {
        public static SpireField<Integer> lockedDamage = new SpireField<>(() -> null);
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class CheckLock {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(DamageInfo info) {
            Integer locked = LockedField.lockedDamage.get(info);
            if (locked != null) {
                info.output = locked;
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(DamageInfo.class, "output");
                return new int[] {LineFinder.findAllInOrder(ctBehavior, m)[2]};
            }
        }
    }
}
