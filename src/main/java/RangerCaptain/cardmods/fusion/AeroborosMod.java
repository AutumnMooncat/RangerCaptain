package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class AeroborosMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AeroborosMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AeroborosMod() {
        super(ID);
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        Wiz.atb(new GainEnergyAction(1));
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AeroborosMod();
    }
}
