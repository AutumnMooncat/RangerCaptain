package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class ApocrowlypseMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(ApocrowlypseMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 5;

    static {
        DynvarInterfaceManager.registerDynvarCarrier(ID);
    }

    public ApocrowlypseMod() {
        super(ID);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if ( card.hasTag(CustomTags.MAGIC_POISON_AOE)) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return String.format(DESCRIPTION_TEXT[0], AMOUNT);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_POISON_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], AMOUNT));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_POISON_AOE)) {
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new PoisonPower(mon, Wiz.adp(), AMOUNT)));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ApocrowlypseMod();
    }
}
