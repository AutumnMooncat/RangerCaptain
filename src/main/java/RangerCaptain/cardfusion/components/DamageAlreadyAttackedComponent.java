package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.AttackDamageRandomPredicateEnemyFollowupAction;
import RangerCaptain.actions.DamageFollowupAction;
import RangerCaptain.actions.DamageRandomPredicateEnemyFollowupAction;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.patches.CardCounterPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DamageAlreadyAttackedComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(DamageAlreadyAttackedComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;

    public DamageAlreadyAttackedComponent(float base, AbstractGameAction.AttackEffect effect) {
        this(base, effect, ComponentTarget.ENEMY);
    }

    public DamageAlreadyAttackedComponent(float base, AbstractGameAction.AttackEffect effect, ComponentTarget target) {
        super(ID, base, ComponentType.DAMAGE, target, DynVar.DAMAGE);
        this.effect = effect;
        setFlags(Flag.RANDOM_WHEN_CAPTURED, Flag.CANT_COLLAPSE_TARGET_TEXT);
    }

    @Override
    public void updatePrio() {
        priority = DAMAGE_PRIO + target.ordinal();
    }

    @Override
    public boolean canUse(FusedCard card, AbstractPlayer p, AbstractMonster m) {
        if (target == ComponentTarget.ENEMY && m != null && CardCounterPatches.AttackCountField.attackedThisTurn.get(m) == 0) {
            card.cantUseMessage = DESCRIPTION_TEXT[ComponentTarget.values().length];
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof DamageAlreadyAttackedComponent) {
            if (target == other.target) {
                return true;
            }
            if (other.target == ComponentTarget.ENEMY_AOE || other.target == ComponentTarget.ENEMY_RANDOM) {
                return target == ComponentTarget.ENEMY;
            }
        }
        return super.shouldStack(other);
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        float mult = 1f;
        if (target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY) {
            mult *= 0.75f;
        }
        if (!(other instanceof DamageLastAttackerComponent)) {
            mult *= 1.25f;
        }
        floatingAmount += other.floatingAmount * mult;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        if (other.type == ComponentType.APPLY && other.isSimple) {
            return target == other.target;
        }
        return false;
    }

    @Override
    public void onCapture(AbstractComponent other) {
        super.onCapture(other);
        if (other.target == ComponentTarget.ENEMY_RANDOM || other.target == ComponentTarget.ENEMY_AOE) {
            other.target = ComponentTarget.ENEMY;
        }
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 1.25f;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        String insert = "";
        List<String> parts = captured.stream().map(AbstractComponent::rawCapturedText).collect(Collectors.toList());
        if (!parts.isEmpty()) {
            insert = AND_APPLY + " " + StringUtils.join(parts, " " + AND + " ") + " ";
        }
        return String.format(CARD_TEXT[target.ordinal()], dynKey(), insert);
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        DamageInfo.DamageType dt = provider instanceof AbstractCard ? ((AbstractCard) provider).damageTypeForTurn : DamageInfo.DamageType.THORNS;
        switch (target) {
            case ENEMY:
                if (CardCounterPatches.AttackCountField.attackedThisTurn.get(m) > 0) {
                    addToBot(new DamageAction(m, new DamageInfo(p, amount, dt), effect));
                    for (AbstractComponent cap : captured) {
                        cap.onTrigger(provider, p, m, Collections.emptyList());
                    }
                }
                break;
            case ENEMY_RANDOM:
                if (provider instanceof AbstractCard) {
                    addToBot(new AttackDamageRandomPredicateEnemyFollowupAction((AbstractCard) provider, effect, mon -> CardCounterPatches.AttackCountField.attackedThisTurn.get(mon) > 0, targ -> {
                        if (targ instanceof AbstractMonster) {
                            ActionCapturePatch.doCapture = true;
                            for (AbstractComponent cap : captured) {
                                cap.onTrigger(provider, p, (AbstractMonster) targ, Collections.emptyList());
                            }
                            ActionCapturePatch.releaseToTop();
                        }
                    }));
                } else {
                    addToBot(new DamageRandomPredicateEnemyFollowupAction(new DamageInfo(p, amount, dt), effect, mon -> CardCounterPatches.AttackCountField.attackedThisTurn.get(mon) > 0, targ -> {
                        if (targ instanceof AbstractMonster) {
                            ActionCapturePatch.doCapture = true;
                            for (AbstractComponent cap : captured) {
                                cap.onTrigger(provider, p, (AbstractMonster) targ, Collections.emptyList());
                            }
                            ActionCapturePatch.releaseToTop();
                        }
                    }));
                }
                break;
            case ENEMY_AOE:
                addToBot(new DoAction(() -> {
                    ArrayList<AbstractMonster> valid = new ArrayList<>();
                    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                        if (!monster.isDeadOrEscaped() && CardCounterPatches.AttackCountField.attackedThisTurn.get(monster) > 0) {
                            valid.add(monster);
                        }
                    }
                    Collections.reverse(valid);
                    for (AbstractMonster monster : valid) {
                        addToTop(new DamageFollowupAction(monster, new DamageInfo(p, amount, dt), effect, false, targ -> {
                            if (targ instanceof AbstractMonster) {
                                ActionCapturePatch.doCapture = true;
                                for (AbstractComponent cap : captured) {
                                    cap.onTrigger(provider, p, (AbstractMonster) targ, Collections.emptyList());
                                }
                                ActionCapturePatch.releaseToTop();
                            }
                        }));
                    }
                }));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DamageAlreadyAttackedComponent(baseAmount, effect, target);
    }
}
