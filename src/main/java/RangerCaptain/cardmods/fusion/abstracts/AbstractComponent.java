package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractComponent implements Comparable<AbstractComponent> {
    private static final String ABSTRACT_ID = MainModfile.makeID(AbstractComponent.class.getSimpleName());
    private static final String[] DYN_KEYS = CardCrawlGame.languagePack.getUIString(ABSTRACT_ID).TEXT;
    private static final String[] BUILDER_TEXT = CardCrawlGame.languagePack.getUIString(ABSTRACT_ID).EXTRA_TEXT;
    private static final String D = DYN_KEYS[1];
    private static final String D2 = DYN_KEYS[2];
    private static final String B = DYN_KEYS[3];
    private static final String B2 = DYN_KEYS[4];
    private static final String M = DYN_KEYS[5];
    private static final String M2 = DYN_KEYS[6];
    private static final String M3 = DYN_KEYS[7];
    public static final String DEAL = BUILDER_TEXT[1];
    public static final String GAIN = BUILDER_TEXT[2];
    public static final String GIVE = BUILDER_TEXT[3];
    public static final String APPLY = BUILDER_TEXT[4];
    public static final String AND = BUILDER_TEXT[5];
    public static final String AND_APPLY = BUILDER_TEXT[6];
    public static final String RANDOM_ENEMY = BUILDER_TEXT[7];
    public static final String ALL_ENEMIES = BUILDER_TEXT[8];
    public static final String BACK = BUILDER_TEXT[9];
    public static final String TAKE = BUILDER_TEXT[10];
    public static final int COST_PRIO = -20;
    public static final int MODIFIER_PRIO = -15;
    public static final int PREFIX_PRIO = -10;
    public static final int BLOCK_PRIO = -5;
    public static final int DAMAGE_PRIO = 0;
    public static final int SIMPLE_APPLY_PRIO = 5;
    public static final int COMPLEX_APPLY_PRIO = 6;
    public static final int DO_PRIO = 15;
    public static final int FINALIZER_PRIO = 20;
    public static final int SUFFIX_PRIO = 25;

    public enum ComponentType {
        BLOCK,
        DAMAGE,
        APPLY,
        DO,
        MODIFIER,
        TRAIT
    }

    public enum ComponentTarget {
        SELF,
        ENEMY,
        ENEMY_RANDOM,
        ENEMY_AOE,
        NONE
    }

    public enum DynVar {
        DAMAGE,
        DAMAGE2,
        BLOCK,
        BLOCK2,
        MAGIC,
        MAGIC2,
        MAGIC3,
        FLAT,
        NONE
    }

    public enum Flag {
        DEFERRED,
        MUST_CAPTURE,
        MUST_BE_CAPTURED,
        REQUIRES_BLOCK,
        REQUIRES_DAMAGE,
        REQUIRES_APPLY,
        INVERSE_PREFERRED,
        INVERSE_FORCED,
        RANDOM_WHEN_CAPTURED,
        AOE_WHEN_CAPTURED,
        TARGETLESS_WHEN_CAPTURED,
        CANT_BE_CAPTURED,
        CANT_COLLAPSE_TARGET_TEXT,
        DRAW_FOLLOWUP,
        DAMAGE_FOLLOWUP,
        EXHAUST_FOLLOWUP,
        EXHAUST_COMPLEX_FOLLOWUP,
        DISCARD_FOLLOWUP,
        STASH_FOLLOWUP,
        THAT_MANY,
        REQUIRES_SAME_SOURCES,
        REQUIRES_DIFFERENT_SOURCES,
        PSEUDO_DAMAGE,
        REMOVE_IF_POWER
    }

    private final String identifier;
    private final HashSet<Flag> flags = new HashSet<>();
    public ComponentType type;
    public ComponentTarget target;
    public DynVar dynvar;
    public MonsterEnum source;
    public int baseAmount;
    public int priority;
    public boolean isSimple;
    public boolean wasCaptured;

    public AbstractComponent(String ID, int baseAmount, ComponentType type, ComponentTarget target, DynVar desiredDynvar) {
        this.identifier = ID;
        this.type = type;
        this.target = target;
        this.baseAmount = baseAmount;
        setDynVar(desiredDynvar);
        updatePrio();
    }

    public abstract void updatePrio();

    public abstract String componentDescription();

    public abstract String rawCardText(List<AbstractComponent> captured);

    public abstract String rawCapturedText();

    public abstract void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured);

    public abstract AbstractComponent makeCopy();

    public AbstractComponent makeEquivalentCopy() {
        AbstractComponent copy = makeCopy();
        copy.setFlags(flags.toArray(new Flag[0]));
        copy.source = source;
        return copy;
    }

    public String identifier() {
        return identifier;
    }

    public String onBuildText(AbstractComponent other, String text) {
        return text;
    }

    public void setFlags(Flag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        updatePrio();
    }

    public boolean hasFlags(Flag... flags) {
        for (Flag flag : flags) {
            if (!this.flags.contains(flag)) {
                return false;
            }
        }
        return true;
    }

    public boolean shouldStack(AbstractComponent other) {
        return dynvar != DynVar.NONE && identifier().equals(other.identifier()) && target == other.target && flags.equals(other.flags) && dynvar == other.dynvar;
    }

    public void receiveStacks(AbstractComponent other) {
        baseAmount += other.baseAmount;
    }

    public boolean captures(AbstractComponent other) {
        return false;
    }

    public boolean canCapture(AbstractComponent other) {
        return !wasCaptured && this != other && captures(other) && !(other.hasFlags(Flag.INVERSE_FORCED) && source == other.source) && !other.hasFlags(Flag.CANT_BE_CAPTURED);
    }

    public void onCapture(AbstractComponent other) {}

    public boolean modifiesAmount(AbstractComponent other) {
        return false;
    }

    public int amountBonus(AbstractComponent other) {
        return 0;
    }

    public float amountMultiplier(AbstractComponent other) {
        return 1f;
    }

    public boolean makesActions() {
        return type == ComponentType.BLOCK || type == ComponentType.DAMAGE || type == ComponentType.APPLY || type == ComponentType.DO;
    }

    public boolean scalesWithCost() {
        return makesActions();
    }

    public boolean isPower() {
        return false;
    }

    public boolean canUse(FusedCard card, AbstractPlayer p, AbstractMonster m) {
        return true;
    }

    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {}

    public void postAssignment(FusedCard card, List<AbstractComponent> otherComponents) {}

    public void glowCheck(FusedCard card) {}

    public String injectXOnDynvars(String text) {
        return text.replace(D+" ",D+"X ")
                .replace(D2+" ", D2+"X ")
                .replace(B+" ", B+"X ")
                .replace(B2+" ", B2+"X ")
                .replace(M+" ", M+"X ")
                .replace(M2+" ", M2+"X ")
                .replace(M3+" ", M3+"X ");
    }

    public void setDynVar(DynVar dynvar) {
        this.dynvar = dynvar;
    }

    public String dynKey() {
        switch (dynvar) {
            case DAMAGE:
                return D;
            case DAMAGE2:
                return D2;
            case BLOCK:
                return B;
            case BLOCK2:
                return B2;
            case MAGIC:
                return M;
            case MAGIC2:
                return M2;
            case MAGIC3:
                return M3;
            case FLAT:
                return String.valueOf(baseAmount);
            default:
                return "";
        }
    }

    public void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    @Override
    public int compareTo(AbstractComponent other) {
        return this.priority - other.priority;
    }

    @Override
    public String toString() {
        return "AbstractComponent{" +
                "identifier='" + identifier + '\'' +
                ", type=" + type +
                ", target=" + target +
                ", dynvar=" + dynvar +
                ", source=" + source +
                ", baseAmount=" + baseAmount +
                ", priority=" + priority +
                ", isSimple=" + isSimple +
                ", flags=" + flags +
                '}';
    }

    public boolean functionallyEquals(AbstractComponent other) {
        return toString().equals(other.toString());
    }

    public static boolean functionallyEquivalent(List<AbstractComponent> left, List<AbstractComponent> right) {
        return StringUtils.join(left.stream().map(AbstractComponent::toString).toArray()).equals(StringUtils.join(right.stream().map(AbstractComponent::toString).toArray()));
    }

    public static List<String> assembleCapturedText(List<AbstractComponent> captured, String targetText) {
        List<String> parts = new ArrayList<>();
        List<String> currentParts = new ArrayList<>();
        boolean applyStarted = false;
        for (AbstractComponent component : captured) {
            if (component.hasFlags(Flag.CANT_COLLAPSE_TARGET_TEXT)) {
                continue;
            }
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
        String finishedPart = StringUtils.join(currentParts, " " + AND + " ");
        if (!finishedPart.isEmpty()) {
            if (!StringUtils.isEmpty(targetText)) {
                finishedPart += " " + targetText;
            }
            parts.add(finishedPart);
        }
        for (AbstractComponent component : captured) {
            if (component.hasFlags(Flag.CANT_COLLAPSE_TARGET_TEXT)) {
                parts.add(component.rawCapturedText());
            }
        }
        return parts;
    }

    public static List<String> assembleCapturedGainText(List<AbstractComponent> captured) {
        List<String> parts = new ArrayList<>();
        List<String> currentParts = new ArrayList<>();
        boolean startedGain = false;
        for (AbstractComponent component : captured) {
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
        if (!currentParts.isEmpty()) {
            parts.add(StringUtils.join(currentParts," " + AND + " "));
        }
        currentParts.clear();
        for (AbstractComponent component : captured) {
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
        return parts;
    }

    public static String compressCapturedText(List<String> parts) {
        String text = "";
        if (parts.size() == 1) {
            text += parts.get(0);
        } else if (parts.size() == 2) {
            text += parts.get(0) + " " + AND + " " + parts.get(1);
        } else if (parts.size() > 2) {
            String last = parts.remove(parts.size() - 1);
            text += StringUtils.join(parts, ", ") + ", " + AND + " " + last;
        }
        return text;
    }

    public static List<AbstractComponent> resolve(FusedCard card, List<AbstractComponent> originals) {
        List<AbstractComponent> components = originals.stream().map(AbstractComponent::makeEquivalentCopy).collect(Collectors.toList());
        resolveCaptures(components);
        resolveStacking(components);
        resolveType(card, components);
        resolveTarget(card, components);
        resolveTraits(card, components);
        resolveAmounts(card.cost, components);
        resolveDynVars(components);
        resolvePostAssignment(card, components);
        resolveRawDescription(card, components);
        card.rollerKey += StringUtils.join(components);
        CardArtRoller.computeCard(card);
        return components;
    }

    public static void resolveCaptures(List<AbstractComponent> components) {
        List<AbstractComponent> captured = new ArrayList<>();
        Map<AbstractComponent, List<AbstractComponent>> captures = new HashMap<>();
        Map<AbstractComponent, List<AbstractComponent>> deferredCaptures = new HashMap<>();
        Map<AbstractComponent, List<AbstractComponent>> inverseCaptures = new HashMap<>();
        for (AbstractComponent component : components) {
            captures.put(component, new ArrayList<>());
            deferredCaptures.put(component, new ArrayList<>());
            inverseCaptures.put(component, new ArrayList<>());
            for (AbstractComponent other : components) {
                if (component.canCapture(other)) {
                    if (other.hasFlags(Flag.INVERSE_PREFERRED) && component.source == other.source) {
                        inverseCaptures.get(component).add(other);
                    } else if (other.hasFlags(Flag.DEFERRED)) {
                        deferredCaptures.get(component).add(other);
                    } else {
                        captured.add(other);
                        captures.get(component).add(other);
                        component.onCapture(other);
                        other.wasCaptured = true;
                    }
                }
            }
        }
        for (AbstractComponent component : components) {
            if (captures.get(component).isEmpty()) {
                for (AbstractComponent other : deferredCaptures.get(component)) {
                    if (!component.wasCaptured) {
                        captured.add(other);
                        captures.get(component).add(other);
                        component.onCapture(other);
                        other.wasCaptured = true;
                    }
                }
            }
            if (captures.get(component).isEmpty()) {
                for (AbstractComponent other : inverseCaptures.get(component)) {
                    if (!component.wasCaptured) {
                        captured.add(other);
                        captures.get(component).add(other);
                        component.onCapture(other);
                    }
                }
            }
        }
        for (AbstractComponent component : captured) {
            if (component.hasFlags(Flag.RANDOM_WHEN_CAPTURED)) {
                component.target = ComponentTarget.ENEMY_RANDOM;
                component.updatePrio();
            } else if (component.hasFlags(Flag.AOE_WHEN_CAPTURED)) {
                component.target = ComponentTarget.ENEMY_AOE;
                component.updatePrio();
            } else if (component.hasFlags(Flag.TARGETLESS_WHEN_CAPTURED)) {
                component.target = ComponentTarget.NONE;
                component.updatePrio();
            }
        }
        components.removeIf(c -> c.hasFlags(Flag.MUST_BE_CAPTURED) && !captured.contains(c));
        components.removeIf(c -> c.hasFlags(Flag.MUST_CAPTURE) && captures.get(c).isEmpty());
        components.removeIf(c -> c.hasFlags(Flag.REQUIRES_BLOCK) && components.stream().noneMatch(check -> c != check && check.type == ComponentType.BLOCK));
        components.removeIf(c -> c.hasFlags(Flag.REQUIRES_DAMAGE) && components.stream().noneMatch(check -> c != check && check.type == ComponentType.DAMAGE));
        components.removeIf(c -> c.hasFlags(Flag.REQUIRES_APPLY) && components.stream().noneMatch(check -> c != check && check.type == ComponentType.APPLY));
        components.removeIf(c -> c.hasFlags(Flag.REQUIRES_SAME_SOURCES) && components.stream().anyMatch(check -> check.source != null && check.source != c.source));
        components.removeIf(c -> c.hasFlags(Flag.REQUIRES_DIFFERENT_SOURCES) && components.stream().noneMatch(check -> check.source != null && check.source != c.source));
        Collections.sort(components);
    }

    public static void resolveStacking(List<AbstractComponent> components) {
        List<AbstractComponent> stacked = new ArrayList<>();
        for (AbstractComponent component : components) {
            for (AbstractComponent other : components) {
                if (component != other && component.shouldStack(other) && !stacked.contains(component) && !stacked.contains(other) && (!other.wasCaptured || component.wasCaptured)) {
                    stacked.add(component);
                    other.receiveStacks(component);
                }
            }
        }
        components.removeAll(stacked);
    }

    public static void resolveTraits(FusedCard card, List<AbstractComponent> components) {
        for (AbstractComponent component : components) {
            if (component.wasCaptured) {
                continue;
            }
            List<AbstractComponent> captured = components.stream().filter(component::canCapture).collect(Collectors.toList());
            component.applyTraits(card, captured);
        }
    }

    public static void resolveRawDescription(FusedCard card, List<AbstractComponent> components) {
        Map<AbstractComponent, String> parts = new HashMap<>();
        for (AbstractComponent component : components) {
            if (!component.wasCaptured) {
                List<AbstractComponent> captured = components.stream().filter(component::canCapture).collect(Collectors.toList());
                String found = component.rawCardText(captured);
                if (found != null && !found.isEmpty()) {
                    parts.put(component, found);
                }
            }
        }
        List<String> text = new ArrayList<>();
        for (AbstractComponent component : components) {
            if (parts.containsKey(component)) {
                String processed = parts.get(component);
                for (AbstractComponent other : components) {
                    if (component != other && !other.wasCaptured) {
                        processed = other.onBuildText(component, processed);
                    }
                }
                text.add(processed + LocalizedStrings.PERIOD);
            }
        }
        card.rawDescription = StringUtils.join(text, " NL ");
    }

    public static void resolveType(FusedCard card, List<AbstractComponent> components) {
        AbstractCard.CardType type = AbstractCard.CardType.SKILL;
        boolean wasPower = false;
        for (AbstractComponent component : components) {
            if (component.isPower()) {
                wasPower = true;
                if (type == AbstractCard.CardType.SKILL) {
                    type = AbstractCard.CardType.POWER;
                }
            }
            if ((component.type == ComponentType.DAMAGE || component.hasFlags(Flag.PSEUDO_DAMAGE)) && !component.wasCaptured) {
                type = AbstractCard.CardType.ATTACK;
            }
        }
        if (wasPower && type != AbstractCard.CardType.POWER) {
            CardModifierManager.addModifier(card, new PurgeMod());
        }
        card.type = type;
        components.removeIf(c -> c.hasFlags(Flag.REMOVE_IF_POWER) && card.type == AbstractCard.CardType.POWER);
    }

    public static void resolveTarget(FusedCard card, List<AbstractComponent> components) {
        AbstractCard.CardTarget target = AbstractCard.CardTarget.NONE;
        for (AbstractComponent component : components) {
            if (component.wasCaptured) {
                continue;
            }
            switch (component.target) {
                case SELF:
                    if (target == AbstractCard.CardTarget.NONE) {
                        target = AbstractCard.CardTarget.SELF;
                    } else if (target == AbstractCard.CardTarget.ENEMY) {
                        target = AbstractCard.CardTarget.SELF_AND_ENEMY;
                    } else if (target == AbstractCard.CardTarget.ALL_ENEMY) {
                        target = AbstractCard.CardTarget.ALL;
                    }
                    break;
                case ENEMY:
                    if (target == AbstractCard.CardTarget.SELF || target == AbstractCard.CardTarget.ALL) {
                        target = AbstractCard.CardTarget.SELF_AND_ENEMY;
                    } else {
                        target = AbstractCard.CardTarget.ENEMY;
                    }
                    break;
                case ENEMY_RANDOM:
                case ENEMY_AOE:
                    if (target == AbstractCard.CardTarget.NONE) {
                        target = AbstractCard.CardTarget.ALL_ENEMY;
                    } else if (target == AbstractCard.CardTarget.SELF) {
                        target = AbstractCard.CardTarget.ALL;
                    }
                    break;
                case NONE:
                    break;
            }
        }
        card.target = target;
    }

    public static void resolveAmounts(int cost, List<AbstractComponent> components) {
        for (AbstractComponent component : components) {
            if (component.dynvar == DynVar.NONE) {
                continue;
            }
            float tmp = component.baseAmount;
            for (AbstractComponent other : components) {
                if (component != other && other.modifiesAmount(component)) {
                    tmp += other.amountBonus(component);
                }
            }
            for (AbstractComponent other : components) {
                if (component != other && other.modifiesAmount(component)) {
                    tmp *= other.amountMultiplier(component);
                }
            }
            if (component.scalesWithCost()) {
                if (cost == 0 || cost == -1) {
                    tmp *= 0.75f;
                } else  {
                    tmp *= (0.75f * cost + 0.25f);
                }
            }
            component.baseAmount = Math.max(1, (int) tmp);
        }
    }

    public static void resolveDynVars(List<AbstractComponent> components) {
        Map<DynVar, Integer> occupied = new HashMap<>();
        for (AbstractComponent c : components) {
            // Capture has run and should have switched any desired dynvars if needed
            DynVar desired = c.dynvar;
            switch (desired) {
                case DAMAGE:
                case DAMAGE2:
                    if (occupied.getOrDefault(DynVar.DAMAGE, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.DAMAGE);
                    } else if (occupied.getOrDefault(DynVar.DAMAGE2, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.DAMAGE2);
                    } else {
                        c.setDynVar(DynVar.FLAT);
                    }
                    break;
                case BLOCK:
                case BLOCK2:
                    if (occupied.getOrDefault(DynVar.BLOCK, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.BLOCK);
                    } else if (occupied.getOrDefault(DynVar.BLOCK2, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.BLOCK2);
                    } else {
                        c.setDynVar(DynVar.FLAT);
                    }
                    break;
                case MAGIC:
                case MAGIC2:
                case MAGIC3:
                    if (occupied.getOrDefault(DynVar.MAGIC, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.MAGIC);
                    } else if (occupied.getOrDefault(DynVar.MAGIC2, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.MAGIC2);
                    } else if (occupied.getOrDefault(DynVar.MAGIC3, c.baseAmount) == c.baseAmount) {
                        c.setDynVar(DynVar.MAGIC3);
                    } else {
                        c.setDynVar(DynVar.FLAT);
                    }
                    break;
            }
            occupied.put(c.dynvar, c.baseAmount);
        }
    }

    public static void resolvePostAssignment(FusedCard card, List<AbstractComponent> components) {
        for (AbstractComponent component : components) {
            component.postAssignment(card, components.stream().filter(c -> c != component).collect(Collectors.toList()));
        }
    }

    public interface ComponentAmountProvider {
        int getAmount(AbstractComponent component);
    }
}
