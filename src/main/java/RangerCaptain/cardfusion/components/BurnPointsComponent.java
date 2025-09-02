package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.powers.BurnedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurnPointsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(BurnPointsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public BurnPointsComponent() {
        this(ComponentTarget.ENEMY_AOE);
    }

    public BurnPointsComponent(ComponentTarget target) {
        super(ID, 0, ComponentType.DO, target, DynVar.NONE);
        setFlags(Flag.TARGETLESS_WHEN_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof BurnPointsComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return CARD_TEXT[target.ordinal()];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        switch (target) {
            case SELF:
                break;
            case ENEMY:
                addToBot(new DoAction(() -> {
                    AbstractPower burn = m.getPower(BurnedPower.POWER_ID);
                    if (burn != null) {
                        addToTop(new LoseHPAction(m, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
                    }
                }));
                break;
            case NONE:
            case ENEMY_AOE:
                addToBot(new DoAction(() -> {
                    for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                        AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
                        if (!mon.isDeadOrEscaped()) {
                            AbstractPower burn = mon.getPower(BurnedPower.POWER_ID);
                            if (burn != null) {
                                addToTop(new LoseHPAction(mon, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
                            }
                        }
                    }
                }));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    ArrayList<AbstractMonster> valid = AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped() && mon.hasPower(BurnedPower.POWER_ID)).collect(Collectors.toCollection(ArrayList::new));
                    if (!valid.isEmpty()) {
                        AbstractMonster mon = valid.get(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
                        AbstractPower burn = mon.getPower(BurnedPower.POWER_ID);
                        if (burn != null) {
                            addToTop(new LoseHPAction(mon, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
                        }
                    }
                }));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BurnPointsComponent(target);
    }
}
