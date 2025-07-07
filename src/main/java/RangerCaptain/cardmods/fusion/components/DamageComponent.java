package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DamageComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(DamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;

    public DamageComponent(int base, AbstractGameAction.AttackEffect effect) {
        this(base, effect, ComponentTarget.ENEMY);
    }

    public DamageComponent(int base, AbstractGameAction.AttackEffect effect, ComponentTarget target) {
        super(ID, base, target == ComponentTarget.SELF ? ComponentType.APPLY : ComponentType.DAMAGE, target, target == ComponentTarget.SELF ? DynVar.MAGIC : DynVar.DAMAGE);
        this.effect = effect;
        isSimple = true;
    }

    @Override
    public void updatePrio() {
        if (target == ComponentTarget.SELF) {
            priority = DO_PRIO;
        } else {
            priority = DAMAGE_PRIO + target.ordinal();
        }
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other.type == ComponentType.DAMAGE) {
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
        if (target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY) {
            baseAmount += (int) (other.baseAmount * 0.75f);
        } else {
            baseAmount += other.baseAmount;
        }
    }

    @Override
    public boolean captures(AbstractComponent other) {
        if (other.type == ComponentType.APPLY && other.isSimple) {
            if (target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY_AOE) {
                return true;
            }
            return target == ComponentTarget.ENEMY_RANDOM && other.target == ComponentTarget.ENEMY_RANDOM;
        }
        return false;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (target == ComponentTarget.SELF || target == ComponentTarget.ENEMY) {
            return String.format(CARD_TEXT[target.ordinal()], dynKey());
        }
        String insert = "";
        List<String> parts = captured.stream().map(AbstractComponent::rawCapturedText).collect(Collectors.toList());
        if (!parts.isEmpty()) {
            insert = AND_APPLY + " " + StringUtils.join(parts, " " + AND + " ") + " ";
        }
        return String.format(CARD_TEXT[target.ordinal()], dynKey(), insert);
    }

    @Override
    public String rawCapturedText() {
        return String.format(CARD_TEXT[ComponentTarget.values().length], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        // TODO damage followups
        int amount = provider.getAmount(this);
        DamageInfo.DamageType dt = provider instanceof AbstractCard ? ((AbstractCard) provider).damageTypeForTurn : DamageInfo.DamageType.THORNS;
        switch (target) {
            case SELF:
                addToBot(new DamageAction(p, new DamageInfo(p, amount, dt), effect));
                break;
            case ENEMY:
                addToBot(new DamageAction(m, new DamageInfo(p, amount, dt), effect));
                break;
            case ENEMY_RANDOM:
                if (provider instanceof AbstractCard) {
                    addToBot(new AttackDamageRandomEnemyAction((AbstractCard) provider, effect));
                }
                else {
                    addToBot(new DamageRandomEnemyAction(new DamageInfo(p, amount, dt), effect));
                }
                break;
            case ENEMY_AOE:
                if (provider instanceof AbstractCard) {
                    int[] scaled = ((AbstractCard) provider).multiDamage;
                    if (((AbstractCard) provider).cost == -1) {
                        int effect = EnergyPanel.totalCount;
                        if (((AbstractCard) provider).energyOnUse != -1) {
                            effect = ((AbstractCard) provider).energyOnUse;
                        }

                        if (Wiz.adp().hasRelic("Chemical X")) {
                            effect += 2;
                            Wiz.adp().getRelic("Chemical X").flash();
                        }
                        for (int i = 0 ; i < scaled.length ; i++) {
                            scaled[i] *= effect;
                        }
                    }
                    addToBot(new DamageAllEnemiesAction(p, scaled, dt, effect));
                }
                else {
                    addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(amount, true), dt, effect));
                }
                break;
        }
        for (AbstractComponent cap : captured) {
            cap.onTrigger(provider, p, m, Collections.emptyList());
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DamageComponent(baseAmount, effect, target);
    }
}
