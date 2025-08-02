package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class FreeWhenPlayedComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(FreeWhenPlayedComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public FreeWhenPlayedComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.ENEMY, DynVar.NONE);
        setFlags(Flag.REMOVE_IF_POWER, Flag.CANT_BE_CAPTURED, Flag.REMOVE_IF_EXHAUST);
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
    public String rawCardText(List<AbstractComponent> captured) {
        return CARD_TEXT[0];
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof FreeWhenPlayedComponent;
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        if (provider instanceof AbstractCard) {
            addToBot(new DoAction(() -> {
                if (m != null && m.getIntentBaseDmg() >= 0) {
                    ((AbstractCard) provider).cost = ((AbstractCard) provider).costForTurn = 0;
                    ((AbstractCard) provider).isCostModified = true;
                }
            }));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new FreeWhenPlayedComponent();
    }
}
