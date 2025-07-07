package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.FusionFormMod;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractPowerComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.GlassBondsPower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class OnPlayPerfectComponent extends AbstractPowerComponent {
    public static final String ID = MainModfile.makeID(OnPlayPerfectComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public OnPlayPerfectComponent() {
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
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        String name = GlassBondsPower.NAME+"?";
        if (provider instanceof FusedCard) {
            FusionFormMod mod = FusionFormMod.getFusionForm((AbstractCard) provider);
            if (mod != null) {
                name = mod.form.fusionName;
            }
        }
        for (AbstractComponent comp : captured) {
            comp.baseAmount = provider.getAmount(comp);
            if (comp.dynvar != DynVar.NONE) {
                comp.dynvar = DynVar.FLAT;
            }
        }
        Wiz.applyToSelf(new GlassBondsPower(p, name, this, captured));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new OnPlayPerfectComponent();
    }
}
