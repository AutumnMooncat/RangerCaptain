package RangerCaptain.util;

import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FusionCardEffectData {
    private static final HashMap<MonsterEnum, List<AbstractComponent>> monsterComponents = new HashMap<>();

    public static void add(MonsterEnum monster, AbstractComponent c) {
        if (!monsterComponents.containsKey(monster)) {
            monsterComponents.put(monster, new ArrayList<>());
        }
        monsterComponents.get(monster).add(c);
    }

    public static List<AbstractComponent> getComponents(MonsterEnum monsterEnum) {
        return monsterComponents.getOrDefault(monsterEnum, new ArrayList<>());
    }

    public static String getFusionTip(MonsterEnum monster) {
        List<AbstractComponent> comps = getComponents(monster);
        if (!comps.isEmpty()) {
            return StringUtils.join(comps.stream().map(AbstractComponent::componentDescription).filter(s -> s != null && !s.isEmpty()).toArray(), ", ");
        } else {
            return "Not yet Implemented.";
        }
    }

    public static List<AbstractComponent> getCombinedComponents(MonsterEnum base, MonsterEnum donor) {
        List<AbstractComponent> comps = new ArrayList<>();
        if (base.ordinal() <= donor.ordinal()) {
            comps.addAll(getComponents(base));
            comps.addAll(getComponents(donor));
        } else {
            comps.addAll(getComponents(donor));
            comps.addAll(getComponents(base));
        }
        Collections.sort(comps);
        return comps;
    }

    public static String getFusionDescription(MonsterEnum base, MonsterEnum donor) {
        List<AbstractComponent> comps = getCombinedComponents(base, donor);
        if (!comps.isEmpty()) {
            return StringUtils.join(comps.stream().map(AbstractComponent::componentDescription).filter(s -> s != null && !s.isEmpty()).distinct().toArray(), ", ");
        } else {
            return "Not yet Implemented.";
        }
    }
}
