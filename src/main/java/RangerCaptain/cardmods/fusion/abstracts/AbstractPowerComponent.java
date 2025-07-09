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

    public abstract String targetText();

    @Override
    public void updatePrio() {
        priority = MODIFIER_PRIO;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.type == ComponentType.BLOCK || other.type == ComponentType.DAMAGE || other.type == ComponentType.APPLY || other.type == ComponentType.DO || other instanceof AbstractDamageModComponent;
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
        String text = "";
        String finishedPart;
        List<String> parts = new ArrayList<>();
        List<String> currentParts = new ArrayList<>();
        List<AbstractComponent> selfTarget = captured.stream().filter(c -> c.target == ComponentTarget.SELF).collect(Collectors.toList());
        List<AbstractComponent> enemyTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY).collect(Collectors.toList());
        List<AbstractComponent> randomTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_RANDOM).collect(Collectors.toList());
        List<AbstractComponent> aoeTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_AOE).collect(Collectors.toList());
        List<AbstractComponent> noTarget = captured.stream().filter(c -> c.target == ComponentTarget.NONE && !(c instanceof AbstractDamageModComponent)).collect(Collectors.toList());
        List<AbstractComponent> mods = captured.stream().filter(c -> c instanceof AbstractDamageModComponent).collect(Collectors.toList());

        // Self
        // Gain %s Block and (Gain) %s <effect>, Take %s damage, other
        boolean startedGain = false;
        for (AbstractComponent component : selfTarget) {
            if (component.type == ComponentType.DAMAGE || component.type == ComponentType.DO) {
                continue;
            }
            String part = "";
            if (!startedGain && component.isSimple) {
                part += GAIN + " ";
            }
            part += component.rawCapturedText();
            currentParts.add(part);
            startedGain = true;
        }
        parts.add(StringUtils.join(currentParts," " + AND + " "));
        currentParts.clear();
        for (AbstractComponent component : selfTarget) {
            if (component.type == ComponentType.DO) {
                parts.add(component.rawCapturedText());
            } else if (component.type == ComponentType.DAMAGE) {
                if (component.isSimple) {
                    parts.add(TAKE + " " + component.rawCapturedText());
                } else {
                    parts.add(component.rawCapturedText());
                }
            }
        }

        // Enemy
        // Give %s Block and Deal %s damage and Apply %s <effect> and other <target>
        boolean applyStarted = false;
        for (AbstractComponent component : enemyTarget) {
            String part = "";
            if (component.isSimple) {
                if (component.type == ComponentType.BLOCK) {
                    part += GIVE + " ";
                } else if (component.type == ComponentType.DAMAGE) {
                    part += DEAL + " ";
                } else if (component.type == ComponentType.APPLY && !applyStarted) {
                    part += APPLY + " ";
                    applyStarted = true;
                }
            }
            part += component.rawCapturedText();
            currentParts.add(part);
        }
        finishedPart = StringUtils.join(currentParts, " " + AND + " ");
        if (!finishedPart.isEmpty()) {
            parts.add(finishedPart + " " + targetText());
        }
        currentParts.clear();
        applyStarted = false;

        // Random
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to a random enemy
        for (AbstractComponent component : randomTarget) {
            String part = "";
            if (component.isSimple) {
                if (component.type == ComponentType.BLOCK) {
                    part += GIVE + " ";
                } else if (component.type == ComponentType.DAMAGE) {
                    part += DEAL + " ";
                } else if (component.type == ComponentType.APPLY && !applyStarted) {
                    part += APPLY + " ";
                    applyStarted = true;
                }
            }
            part += component.rawCapturedText();
            currentParts.add(part);
        }
        finishedPart = StringUtils.join(currentParts, " " + AND + " ");
        if (!finishedPart.isEmpty()) {
            parts.add(finishedPart + " " + RANDOM_ENEMY);
        }
        currentParts.clear();
        applyStarted = false;

        // AOE
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to ALL enemies
        for (AbstractComponent component : aoeTarget) {
            String part = "";
            if (component.isSimple) {
                if (component.type == ComponentType.BLOCK) {
                    part += GIVE + " ";
                } else if (component.type == ComponentType.DAMAGE) {
                    part += DEAL + " ";
                } else if (component.type == ComponentType.APPLY && !applyStarted) {
                    part += APPLY + " ";
                    applyStarted = true;
                }
            }
            part += component.rawCapturedText();
            currentParts.add(part);
        }
        finishedPart = StringUtils.join(currentParts, " " + AND + " ");
        if (!finishedPart.isEmpty()) {
            parts.add(finishedPart + " " + ALL_ENEMIES);
        }
        currentParts.clear();

        // None
        // Just list as is
        for (AbstractComponent component : noTarget) {
            parts.add(component.rawCapturedText());
        }

        if (parts.size() == 1) {
            text += parts.get(0);
        } else if (parts.size() == 2) {
            text += parts.get(0) + " " + AND + " " + parts.get(1);
        } else if (parts.size() > 2) {
            String last = parts.remove(parts.size() - 1);
            text += StringUtils.join(parts, ", ") + ", " + AND + " " + last;
        }

        if (text.isEmpty()) {
            text = "Invalid Power";
        }

        for (AbstractComponent mod : mods) {
            currentParts.add(mod.rawCapturedText());
        }
        if (!currentParts.isEmpty()) {
            currentParts.add(0, text);
            text = StringUtils.join(currentParts, ". NL ");
            currentParts.clear();
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
                if (GameDictionary.keywords.containsKey(lower)) {
                    if (BaseMod.keywordIsUnique(lower)) {
                        parts[i] = StringUtils.join(Arrays.stream(lower.replace(BaseMod.getKeywordPrefix(lower), "").split(" ")).map(s -> "#y"+FormatHelper.capitalize(s)).toArray(), " ");
                    } else {
                        parts[i] = FormatHelper.prefixWords(parts[i], "#y");
                    }
                }
            }
        }
        return StringUtils.join(parts, " ") + LocalizedStrings.PERIOD;
    }
}
