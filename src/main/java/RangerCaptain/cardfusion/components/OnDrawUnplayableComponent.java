package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractPowerComponent;
import RangerCaptain.powers.SpringLoadedPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class OnDrawUnplayableComponent extends AbstractPowerComponent {
    public static final String ID = MainModfile.makeID(OnDrawUnplayableComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public OnDrawUnplayableComponent() {
        super(ID, false);
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 0.75f;
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
        return "";
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        String name = getPowerName(provider, SpringLoadedPower.NAME+"?");
        flattenComponents(provider, captured);
        Wiz.applyToSelf(new SpringLoadedPower(p, name, this, captured));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new OnDrawUnplayableComponent();
    }
}
