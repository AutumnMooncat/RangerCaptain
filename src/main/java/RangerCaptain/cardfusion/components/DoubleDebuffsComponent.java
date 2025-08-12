package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.actions.DoublePowerAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleDebuffsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(DoubleDebuffsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private boolean wasAOE;

    public DoubleDebuffsComponent(int base) {
        this(base, ComponentTarget.ENEMY);
    }

    public DoubleDebuffsComponent(int base, ComponentTarget target) {
        super(ID, base, ComponentType.DO, target, DynVar.FLAT);
        setFlags(Flag.TARGETLESS_WHEN_CAPTURED);
    }

    @Override
    public void updatePrio() {
        if (target == ComponentTarget.ENEMY_AOE) {
            wasAOE = true;
        }
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof DoubleDebuffsComponent) {
            return target == other.target;
        }
        return false;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        int index = target.ordinal() * 2;
        if (target == ComponentTarget.NONE) {
            index -= wasAOE ? 2 : 4;
        }
        return baseAmount == 1 ? CARD_TEXT[index] : String.format(CARD_TEXT[index + 1], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        switch (target) {
            case SELF:
                addToBot(new DoublePowerAction(p, amount, pow -> pow.type == AbstractPower.PowerType.DEBUFF));
                break;
            case ENEMY:
                addToBot(new DoublePowerAction(m, amount, pow -> pow.type == AbstractPower.PowerType.DEBUFF));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    List<AbstractMonster> valid = AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped() && mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)).collect(Collectors.toList());
                    if (!valid.isEmpty()) {
                        addToTop(new DoublePowerAction(valid.get(AbstractDungeon.cardRandomRng.random(valid.size() - 1)), amount, true, pow -> pow.type == AbstractPower.PowerType.DEBUFF, l -> {}));
                    }
                }));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> addToBot(new DoublePowerAction(mon, amount, true, pow -> pow.type == AbstractPower.PowerType.DEBUFF, l -> {})));
                break;
            case NONE:
                if (wasAOE) {
                    Wiz.forAllMonstersLiving(mon -> addToBot(new DoublePowerAction(mon, amount, true, pow -> pow.type == AbstractPower.PowerType.DEBUFF, l -> {})));
                } else {
                    addToBot(new DoAction(() -> {
                        List<AbstractMonster> valid = AbstractDungeon.getMonsters().monsters.stream().filter(mon -> !mon.isDeadOrEscaped() && mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)).collect(Collectors.toList());
                        if (!valid.isEmpty()) {
                            addToTop(new DoublePowerAction(valid.get(AbstractDungeon.cardRandomRng.random(valid.size() - 1)), amount, true, pow -> pow.type == AbstractPower.PowerType.DEBUFF, l -> {}));
                        }
                    }));
                }
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DoubleDebuffsComponent(baseAmount, target);
    }
}
