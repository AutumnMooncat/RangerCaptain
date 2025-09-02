package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class MultitargetComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(MultitargetComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public MultitargetComponent(float base) {
        super(ID, base, ComponentType.DO, ComponentTarget.SELF, DynVar.MAGIC);
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
        if (dynvar == DynVar.FLAT) {
            return workingAmount == 1 ? CARD_TEXT[1] : String.format(CARD_TEXT[2], workingAmount);
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        Wiz.applyToSelf(new MultitargetPower(Wiz.adp(), provider.getAmount(this)));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new MultitargetComponent(baseAmount);
    }
}
