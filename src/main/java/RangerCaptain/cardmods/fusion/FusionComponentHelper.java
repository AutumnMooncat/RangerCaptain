package RangerCaptain.cardmods.fusion;

import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.*;
import RangerCaptain.util.FusionCardEffectData;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class FusionComponentHelper {
    private final MonsterEnum monster;
    private final List<AbstractComponent> components;

    public FusionComponentHelper(MonsterEnum monster) {
        this.monster = monster;
        this.components = new ArrayList<>();
    }

    public FusionComponentHelper withDamage(int damage, AbstractGameAction.AttackEffect effect) {
        this.components.add(new DamageComponent(damage, effect));
        return this;
    }

    public FusionComponentHelper withDamageRandom(int damage, AbstractGameAction.AttackEffect effect) {
        this.components.add(new DamageComponent(damage, effect, AbstractComponent.ComponentTarget.ENEMY_RANDOM));
        return this;
    }

    public FusionComponentHelper withDamageAOE(int damage, AbstractGameAction.AttackEffect effect) {
        this.components.add(new DamageComponent(damage, effect, AbstractComponent.ComponentTarget.ENEMY_AOE));
        return this;
    }

    public FusionComponentHelper withMultiDamage(int damage, int hits, AbstractGameAction.AttackEffect effect) {
        this.components.add(new MultiDamageComponent(damage, hits, effect));
        return this;
    }

    public FusionComponentHelper withMultiDamageRandom(int damage, int hits, AbstractGameAction.AttackEffect effect) {
        this.components.add(new MultiDamageComponent(damage, hits, effect, AbstractComponent.ComponentTarget.ENEMY_RANDOM));
        return this;
    }

    public FusionComponentHelper withMultiDamageAOE(int damage, int hits, AbstractGameAction.AttackEffect effect) {
        this.components.add(new MultiDamageComponent(damage, hits, effect, AbstractComponent.ComponentTarget.ENEMY_AOE));
        return this;
    }

    public FusionComponentHelper withBlock(int block) {
        this.components.add(new BlockComponent(block));
        return this;
    }

    public FusionComponentHelper withCost(int cost) {
        if (cost == -1) {
            this.components.add(new XComponent());
        } else {
            this.components.add(new CostComponent(cost));
        }
        return this;
    }

    public FusionComponentHelper withRetain() {
        this.components.add(new AddRetainComponent());
        return this;
    }

    public FusionComponentHelper withEthereal() {
        this.components.add(new AddEtherealComponent());
        return this;
    }

    public FusionComponentHelper withExhaust() {
        this.components.add(new AddExhaustComponent());
        return this;
    }

    public FusionComponentHelper withMindMeld() {
        this.components.add(new AddMindMeldComponent());
        return this;
    }

    public FusionComponentHelper withCloseEncounter() {
        this.components.add(new AddCloseEncounterComponent());
        return this;
    }

    public FusionComponentHelper withDoublePlay() {
        this.components.add(new AddDoublePlayComponent());
        return this;
    }

    public FusionComponentHelper with(AbstractComponent... components) {
        this.components.addAll(Arrays.asList(components));
        return this;
    }

    public FusionComponentHelper withFlags(AbstractComponent component, AbstractComponent.Flag... flags) {
        component.setFlags(flags);
        this.components.add(component);
        return this;
    }

    @SafeVarargs
    public final FusionComponentHelper withMake(Supplier<AbstractComponent>... suppliers) {
        for (Supplier<AbstractComponent> supplier : suppliers) {
            this.components.add(supplier.get());
        }
        return this;
    }

    public FusionComponentHelper withMake(Supplier<List<AbstractComponent>> supplier) {
        this.components.addAll(supplier.get());
        return this;
    }

    public void register() {
        Collections.sort(components);
        for (AbstractComponent c : components) {
            c.source = monster;
            FusionCardEffectData.add(monster, c);
        }
    }
}
