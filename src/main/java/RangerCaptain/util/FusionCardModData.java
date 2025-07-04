package RangerCaptain.util;

import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FusionCardModData {
    private static final HashMap<MonsterEnum, List<AbstractFusionMod>> MOD_MAP = new HashMap<>();

    public static void add(MonsterEnum monster, AbstractFusionMod mod) {
        if (!MOD_MAP.containsKey(monster)) {
            MOD_MAP.put(monster, new ArrayList<>());
        }
        MOD_MAP.get(monster).add(mod);
    }

    public static List<AbstractFusionMod> getMods(MonsterEnum monsterEnum) {
        return MOD_MAP.getOrDefault(monsterEnum, new ArrayList<>());
    }

    public static String getModDescription(MonsterEnum monster) {
        List<AbstractFusionMod> mods = getMods(monster);
        if (!mods.isEmpty()) {
            return StringUtils.join(mods.stream().map(AbstractFusionMod::getModDescription), " ");
        } else {
            return "Not yet Implemented.";
        }
    }
}
