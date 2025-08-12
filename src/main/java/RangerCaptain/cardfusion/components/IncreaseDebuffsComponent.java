package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.actions.IncreaseDebuffsAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class IncreaseDebuffsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(IncreaseDebuffsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public IncreaseDebuffsComponent(int base) {
        this(base, ComponentTarget.ENEMY);
    }

    public IncreaseDebuffsComponent(int base, ComponentTarget target) {
        super(ID, base, ComponentType.DO, target, DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO + target.ordinal();
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
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        switch (target) {
            case SELF:
                addToBot(new IncreaseDebuffsAction(p, amount));
                break;
            case ENEMY:
                addToBot(new IncreaseDebuffsAction(m, amount));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> addToTop(new IncreaseDebuffsAction(AbstractDungeon.getRandomMonster(), amount))));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> addToBot(new IncreaseDebuffsAction(mon, amount)));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new IncreaseDebuffsComponent(baseAmount, target);
    }
}
