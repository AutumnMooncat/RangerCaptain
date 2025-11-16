package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractPowerComponent;
import RangerCaptain.powers.AcidReflexPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class OnTurnStartComponent extends AbstractPowerComponent {
    public static final String ID = MainModfile.makeID(OnTurnStartComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public OnTurnStartComponent() {
        super(ID, true);
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 0.5f;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[0], assembleCapturedText(captured));
    }

    @Override
    public String targetText() {
        return CARD_TEXT[1];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        String name = getPowerName(provider, AcidReflexPower.NAME+"?");
        flattenComponents(provider, captured);
        Wiz.applyToSelf(new AcidReflexPower(p, name, this, captured));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new OnTurnStartComponent();
    }
}
