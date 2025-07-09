package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.List;

public class MultiDamageComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(MultiDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;
    private int hits;

    public MultiDamageComponent(int base, int hits, AbstractGameAction.AttackEffect effect) {
        this(base, hits, effect, ComponentTarget.ENEMY);
    }

    public MultiDamageComponent(int base, int hits, AbstractGameAction.AttackEffect effect, ComponentTarget target) {
        super(ID, base, target == ComponentTarget.SELF ? ComponentType.APPLY : ComponentType.DAMAGE, target, target == ComponentTarget.SELF ? DynVar.MAGIC : DynVar.DAMAGE);
        this.effect = effect;
        this.hits = hits;
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
        if (other instanceof MultiDamageComponent) {
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
        float mult = 0.5f;
        if (target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY) {
            mult *= 0.75f;
        }
        if (other instanceof MultiDamageComponent) {
            hits += ((MultiDamageComponent) other).hits - 1;
        }
        baseAmount += (int) (other.baseAmount * mult);
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[target.ordinal()], dynKey(), hits);
    }

    @Override
    public String rawCapturedText() {
        return String.format(CARD_TEXT[ComponentTarget.values().length], dynKey(), hits);
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        // TODO damage followups
        int amount = provider.getAmount(this);
        DamageInfo.DamageType dt = provider instanceof AbstractCard ? ((AbstractCard) provider).damageTypeForTurn : DamageInfo.DamageType.THORNS;
        switch (target) {
            case SELF:
                for (int i = 0; i < hits; i++) {
                    addToBot(new DamageAction(p, new DamageInfo(p, amount, dt), effect));
                }
                break;
            case ENEMY:
                for (int i = 0; i < hits; i++) {
                    addToBot(new DamageAction(m, new DamageInfo(p, amount, dt), effect));
                }
                break;
            case ENEMY_RANDOM:
                // TODO effect may not match text if captured
                if (provider instanceof AbstractCard) {
                    for (int i = 0; i < hits; i++) {
                        addToBot(new AttackDamageRandomEnemyAction((AbstractCard) provider, effect));
                    }
                }
                else {
                    addToBot(new DoAction(() -> {
                        AbstractMonster mon = AbstractDungeon.getMonsters().getRandomMonster();
                        if (mon != null) {
                            for (int i = 0; i < hits; i++) {
                                this.addToTop(new DamageAction(mon, new DamageInfo(p, amount, dt), effect));
                            }
                        }
                    }));
                }
                break;
            case ENEMY_AOE:
                int[] damages = DamageInfo.createDamageMatrix(amount, true);
                if (provider instanceof AbstractCard) {
                    damages = ((AbstractCard) provider).multiDamage;
                    if (((AbstractCard) provider).cost == -1) {
                        int effect = EnergyPanel.totalCount;
                        if (((AbstractCard) provider).energyOnUse != -1) {
                            effect = ((AbstractCard) provider).energyOnUse;
                        }

                        if (Wiz.adp().hasRelic("Chemical X")) {
                            effect += 2;
                            Wiz.adp().getRelic("Chemical X").flash();
                        }
                        for (int i = 0 ; i < damages.length ; i++) {
                            damages[i] *= effect;
                        }
                    }
                }
                for (int i = 0; i < hits; i++) {
                    addToBot(new DamageAllEnemiesAction(p, damages, dt, effect));
                }
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new MultiDamageComponent(baseAmount, hits, effect, target);
    }
}
