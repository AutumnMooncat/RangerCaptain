package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.powers.SuitUpPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class SuitUpComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(SuitUpComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public SuitUpComponent(int base) {
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
            return baseAmount == 1 ? CARD_TEXT[1] : String.format(CARD_TEXT[2], baseAmount);
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String rawCapturedText() {
        if (dynvar == DynVar.FLAT) {
            return baseAmount == 1 ? CARD_TEXT[4] : String.format(CARD_TEXT[5], baseAmount);
        }
        return String.format(CARD_TEXT[3], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        Wiz.applyToSelf(new SuitUpPower(Wiz.adp(), provider.getAmount(this)));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new SuitUpComponent(baseAmount);
    }
}
