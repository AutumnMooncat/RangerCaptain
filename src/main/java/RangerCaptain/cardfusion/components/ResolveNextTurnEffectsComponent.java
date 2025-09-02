package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.ResolveNextTurnEffectsAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class ResolveNextTurnEffectsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(ResolveNextTurnEffectsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ResolveNextTurnEffectsComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.SELF, DynVar.NONE);
        setFlags(Flag.CANT_BE_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO + 2;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof ResolveNextTurnEffectsComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return CARD_TEXT[0];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new ResolveNextTurnEffectsAction());
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ResolveNextTurnEffectsComponent();
    }
}
