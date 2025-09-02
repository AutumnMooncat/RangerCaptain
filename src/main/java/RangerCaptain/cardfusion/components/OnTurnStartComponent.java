package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractPowerComponent;
import RangerCaptain.cardmods.FusionFormMod;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.AcidReflexPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class OnTurnStartComponent extends AbstractPowerComponent {
    public static final String ID = MainModfile.makeID(OnTurnStartComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public OnTurnStartComponent() {
        super(ID, false);
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
        return "";
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        String name = AcidReflexPower.NAME+"?";
        if (provider instanceof FusedCard) {
            FusionFormMod mod = FusionFormMod.getFusionForm((AbstractCard) provider);
            if (mod != null) {
                name = mod.form.fusionName;
            }
        }
        for (AbstractComponent comp : captured) {
            comp.workingAmount = provider.getAmount(comp);
            if (comp.dynvar != DynVar.NONE) {
                comp.dynvar = DynVar.FLAT;
            }
        }
        Wiz.applyToSelf(new AcidReflexPower(p, name, this, captured));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new OnTurnStartComponent();
    }
}
