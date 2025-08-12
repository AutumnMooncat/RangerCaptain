package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.powers.NextTurnPowerLaterPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class NextTwoTurnsEnergyComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(NextTwoTurnsEnergyComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public NextTwoTurnsEnergyComponent(float base) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
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
            if (workingAmount >= 1 && workingAmount <= 3) {
                return CARD_TEXT[workingAmount];
            }
            return String.format(CARD_TEXT[4], workingAmount);
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String rawCapturedText() {
        int offset = 5;
        if (dynvar == DynVar.FLAT) {
            if (workingAmount >= 1 && workingAmount <= 3) {
                return CARD_TEXT[workingAmount + offset];
            }
            return String.format(CARD_TEXT[4 + offset], workingAmount);
        }
        return String.format(CARD_TEXT[offset], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        Wiz.applyToSelf(new APBoostPower(p, provider.getAmount(this)));
        Wiz.applyToSelf(new NextTurnPowerLaterPower(p, new APBoostPower(p, provider.getAmount(this))));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new NextTwoTurnsEnergyComponent(baseAmount);
    }
}
