package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class FreeOnceWhenStashedComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(FreeOnceWhenStashedComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public FreeOnceWhenStashedComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.NONE, DynVar.NONE);
        setFlags(Flag.CANT_BE_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = FINALIZER_PRIO;
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
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof FreeOnceWhenStashedComponent;
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public void triggerOnStashed(FusedCard card) {
        card.superFlash();
        card.freeToPlayOnce = true;
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {}

    @Override
    public AbstractComponent makeCopy() {
        return new FreeOnceWhenStashedComponent();
    }
}
