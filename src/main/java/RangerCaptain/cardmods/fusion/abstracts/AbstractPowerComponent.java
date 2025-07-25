package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.util.FormatHelper;
import basemod.BaseMod;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractPowerComponent extends AbstractComponent {
    public boolean providesTarget;

    public AbstractPowerComponent(String ID, boolean providesTarget) {
        super(ID, 0, ComponentType.MODIFIER, ComponentTarget.SELF, DynVar.NONE);
        this.providesTarget = providesTarget;
    }

    @Override
    public boolean isPower() {
        return true;
    }

    public abstract String targetText();

    @Override
    public void updatePrio() {
        priority = MODIFIER_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return identifier().equals(other.identifier());
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.makesActions() || other instanceof AbstractDamageModComponent;
    }

    @Override
    public void onCapture(AbstractComponent other) {
        if (other.target == ComponentTarget.ENEMY && !providesTarget) {
            other.target = ComponentTarget.ENEMY_RANDOM;
            other.updatePrio();
        }
    }

    @Override
    public boolean modifiesAmount(AbstractComponent other) {
        return captures(other);
    }

    public String assembleCapturedText(List<AbstractComponent> captured) {
        List<String> parts = new ArrayList<>();
        List<AbstractComponent> selfTarget = captured.stream().filter(c -> c.target == ComponentTarget.SELF && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> enemyTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> randomTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_RANDOM && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> aoeTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_AOE && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> noTarget = captured.stream().filter(c -> c.target == ComponentTarget.NONE && !StringUtils.isEmpty(c.rawCapturedText()) && !(c instanceof AbstractDamageModComponent)).collect(Collectors.toList());
        List<AbstractComponent> mods = captured.stream().filter(c -> c instanceof AbstractDamageModComponent && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());

        // Self
        // Gain %s Block and (Gain) %s <effect>, Take %s damage, other
        parts.addAll(assembleCapturedGainText(selfTarget));

        // Enemy
        // Give %s Block and Deal %s damage and Apply %s <effect> and other <target>
        parts.addAll(assembleCapturedText(enemyTarget, targetText()));

        // Random
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to a random enemy
        parts.addAll(assembleCapturedText(randomTarget, RANDOM_ENEMY));

        // AOE
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to ALL enemies
        parts.addAll(assembleCapturedText(aoeTarget, ALL_ENEMIES));

        // None
        // Just list as is
        for (AbstractComponent component : noTarget) {
            parts.add(component.rawCapturedText());
        }

        String text = compressCapturedText(parts);

        if (text.isEmpty()) {
            text = "Invalid Power";
        }

        parts.clear();

        for (AbstractComponent mod : mods) {
            parts.add(mod.rawCapturedText());
        }

        if (!parts.isEmpty()) {
            parts.add(0, text);
            text = StringUtils.join(parts, ". NL ");
        }

        return text;
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    public String rawPowerText(List<AbstractComponent> captured) {
        String[] parts = rawCardText(captured).split(" ");
        for (int i = 0; i < parts.length; i++) {
            if (StringUtils.isNumeric(parts[i])) {
                parts[i] = "#b"+parts[i];
            } else if (parts[i].startsWith("*") && parts.length > 1) {
                parts[i] = "#y"+parts[i].substring(1);
            } else {
                String lower = parts[i].toLowerCase();
                String last = "";
                if (lower.endsWith(",")) {
                    last = ",";
                    lower = lower.substring(0, lower.length() - 1);
                }
                if (GameDictionary.keywords.containsKey(lower)) {
                    if (BaseMod.keywordIsUnique(lower)) {
                        parts[i] = StringUtils.join(Arrays.stream(lower.replace(BaseMod.getKeywordPrefix(lower), "").replace("_"," ").split(" ")).map(s -> "#y"+FormatHelper.capitalize(s)).toArray(), " ")+last;
                    } else {
                        parts[i] = FormatHelper.prefixWords(parts[i], "#y");
                    }
                }
            }
        }
        String combined = StringUtils.join(parts, " ");
        if (combined.endsWith("[E]")) {
            combined += " ";
        }
        return combined + LocalizedStrings.PERIOD;
    }
}
