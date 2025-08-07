package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.powers.NextTurnTakeDamagePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.List;

public class NextTurnDamageComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(NextTurnDamageComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;

    public NextTurnDamageComponent(int base, AbstractGameAction.AttackEffect effect) {
        this(base, effect, ComponentTarget.ENEMY);
    }

    public NextTurnDamageComponent(int base, AbstractGameAction.AttackEffect effect, ComponentTarget target) {
        super(ID, base, target == ComponentTarget.SELF ? ComponentType.APPLY : ComponentType.DAMAGE, target, target == ComponentTarget.SELF ? DynVar.MAGIC : DynVar.DAMAGE);
        this.effect = effect;
        isSimple = true;
    }

    @Override
    public void updatePrio() {
        if (target == ComponentTarget.SELF) {
            priority = DO_PRIO;
        } else {
            priority = DAMAGE_PRIO + target.ordinal() + 2;
        }
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof NextTurnDamageComponent) {
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
        floatingAmount += other.floatingAmount * mult;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[target.ordinal()], dynKey());
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
                Wiz.applyToSelf(new NextTurnTakeDamagePower(p, new DamageInfo(p, amount, DamageInfo.DamageType.THORNS), effect));
                break;
            case ENEMY:
                Wiz.applyToEnemy(m, new NextTurnTakeDamagePower(m, new DamageInfo(p, amount, dt), effect));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    AbstractMonster mon = AbstractDungeon.getRandomMonster();
                    if (mon != null) {
                        Wiz.applyToEnemyTop(mon, new NextTurnTakeDamagePower(mon, new DamageInfo(p, amount, dt), effect));
                    }
                }));
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
                    for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                        AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
                        if (!mon.isDeadOrEscaped()) {
                            Wiz.applyToEnemy(mon, new NextTurnTakeDamagePower(mon, new DamageInfo(p, scaled[i], dt), effect));
                        }
                    }
                }
                else {
                    Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new NextTurnTakeDamagePower(mon, new DamageInfo(p, amount, dt), effect)));
                }
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new NextTurnDamageComponent(baseAmount, effect, target);
    }
}
