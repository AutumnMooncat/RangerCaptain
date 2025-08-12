package RangerCaptain.cardfusion.abstracts;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractApplyComponent extends AbstractComponent {
    public static final String BASE_ID = MainModfile.makeID(AbstractApplyComponent.class.getSimpleName());
    public static final String[] BASE_DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(BASE_ID).TEXT;
    public static final String[] BASE_CARD_TEXT = CardCrawlGame.languagePack.getUIString(BASE_ID).EXTRA_TEXT;

    public AbstractApplyComponent(String ID, float base, ComponentTarget target) {
        super(ID, base, ComponentType.APPLY, target, DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = COMPLEX_APPLY_PRIO + target.ordinal() * 2;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (identifier().equals(other.identifier())) {
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
            floatingAmount += other.floatingAmount * 0.75f;
        } else {
            floatingAmount += other.floatingAmount;
        }
    }

    @Override
    public boolean captures(AbstractComponent other) {
        if (other instanceof AbstractApplyComponent && target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY_AOE) {
            return true;
        }
        return super.captures(other);
    }

    public String getCapturedText(List<AbstractComponent> captured) {
        String text = "";
        List<String> parts = new ArrayList<>();
        for (AbstractComponent component : captured) {
            parts.add(component.rawCapturedText());
        }
        if (!parts.isEmpty()) {
            if (parts.size() == 1) {
                text += " " + AND + " " + parts.get(0);
            } else {
                String last = parts.remove(parts.size()-1);
                text += ", " + StringUtils.join(parts, ", ") + ", " + AND + " " + last;
            }
        }
        return text;
    }

    public abstract String getName();

    public abstract String getKeyword();

    public abstract void doApply(AbstractCreature target, int amount, boolean toTop);

    @Override
    public String componentDescription() {
        return String.format(BASE_DESCRIPTION_TEXT[target.ordinal()], getName());
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (target == ComponentTarget.ENEMY_AOE) {
            return String.format(BASE_CARD_TEXT[target.ordinal()], dynKey(), getKeyword(), getCapturedText(captured));
        }
        return String.format(BASE_CARD_TEXT[target.ordinal()], dynKey(), getKeyword());
    }

    @Override
    public String rawCapturedText() {
        return String.format(BASE_CARD_TEXT[ComponentTarget.values().length], dynKey(), getKeyword());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        switch (target) {
            case SELF:
                doApply(p, amount, false);
                break;
            case ENEMY:
                doApply(m, amount, false);
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> doApply(AbstractDungeon.getRandomMonster(), amount, true)));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> doApply(mon, amount, false));
                for (AbstractComponent component : captured) {
                    component.onTrigger(provider, p, m, Collections.emptyList());
                }
                break;
        }
    }
}
