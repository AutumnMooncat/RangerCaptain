package RangerCaptain.util;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FusionCardEffectData {
    private static final String ID = MainModfile.makeID(FusionCardEffectData.class.getSimpleName());
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
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
            List<String> parts = new ArrayList<>();
            for (AbstractComponent comp : comps) {
                String text = comp.componentDescription();
                if (text != null && !text.isEmpty()) {
                    if (comp.hasFlags(AbstractComponent.Flag.REQUIRES_SAME_SOURCES)) {
                        text += " " + TEXT[1];
                    } else if (comp.hasFlags(AbstractComponent.Flag.REQUIRES_DIFFERENT_SOURCES)) {
                        text += " " + TEXT[2];
                    }
                    parts.add(text);
                }
            }
            return StringUtils.join(parts, ", ");
        } else {
            return TEXT[0];
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
        if (base == donor) {
            comps.removeIf(c -> c.hasFlags(AbstractComponent.Flag.REQUIRES_DIFFERENT_SOURCES));
        } else {
            comps.removeIf(c -> c.hasFlags(AbstractComponent.Flag.REQUIRES_SAME_SOURCES));
        }
        if (!comps.isEmpty()) {
            return StringUtils.join(comps.stream().map(AbstractComponent::componentDescription).filter(s -> s != null && !s.isEmpty()).distinct().toArray(), ", ");
        } else {
            return TEXT[0];
        }
    }
}
