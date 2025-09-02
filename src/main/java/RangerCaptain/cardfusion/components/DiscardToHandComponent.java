package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.interfaces.ThatManyComponent;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class DiscardToHandComponent extends AbstractComponent implements ThatManyComponent {
    public static final String ID = MainModfile.makeID(DiscardToHandComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public DiscardToHandComponent(float base) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, base == 0 ? DynVar.NONE : DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof DiscardToHandComponent) {
            return dynvar == other.dynvar;
        }
        return false;
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (dynvar == DynVar.FLAT) {
            return workingAmount == 1 ? CARD_TEXT[1] : String.format(CARD_TEXT[2], workingAmount);
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String thatManyText() {
        return CARD_TEXT[3];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new BetterDiscardPileToHandAction(provider.getAmount(this)));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DiscardToHandComponent(baseAmount);
    }
}
