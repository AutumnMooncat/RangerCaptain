package RangerCaptain.cardmods.fusion;

import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cardmods.fusion.mods.*;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FusionModHelper {
    private final MonsterEnum monster;
    private final List<AbstractFusionMod> mods;

    public FusionModHelper(MonsterEnum monster) {
        this.monster = monster;
        this.mods = new ArrayList<>();
    }

    public FusionModHelper withDamage(int damage, AbstractGameAction.AttackEffect effect) {
        this.mods.add(new DamageMod(damage, effect));
        return this;
    }

    public FusionModHelper withDamageAOE(int damage, AbstractGameAction.AttackEffect effect) {
        this.mods.add(new DamageAOEMod(damage, effect));
        return this;
    }

    public FusionModHelper withBlock(int block) {
        this.mods.add(new BlockMod(block));
        return this;
    }

    public FusionModHelper withChangeCost(int change) {
        this.mods.add(new ChangeCostMod(change));
        return this;
    }

    public FusionModHelper withSetCost(int newCost) {
        this.mods.add(new SetCostMod(newCost));
        return this;
    }

    public FusionModHelper withRetain() {
        this.mods.add(new AddRetainMod());
        return this;
    }

    public FusionModHelper withEthereal() {
        this.mods.add(new AddEtherealMod());
        return this;
    }

    public FusionModHelper withExhaust() {
        this.mods.add(new AddExhaustMod());
        return this;
    }

    public FusionModHelper withMindMeld() {
        this.mods.add(new AddMindMeldMod());
        return this;
    }

    public FusionModHelper withCloseEncounter() {
        this.mods.add(new AddCloseEncounterMod());
        return this;
    }

    public FusionModHelper withDoublePlay() {
        this.mods.add(new AddDoublePlayMod());
        return this;
    }

    public FusionModHelper with(AbstractFusionMod... mods) {
        this.mods.addAll(Arrays.asList(mods));
        return this;
    }

    public void register() {
        for (AbstractFusionMod mod : mods) {
            //FusionCardEffectData.add(monster, mod);
        }
    }
}
