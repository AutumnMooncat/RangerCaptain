package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AddDoublePlayMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AddDoublePlayMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public AddDoublePlayMod() {
        super(ID);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractCard copy = card.makeStatEquivalentCopy();
        CardModifierManager.removeModifiersById(copy, ID, false);
        GameActionManager.queueExtraCard(copy, (AbstractMonster) target);
    }

    @Override
    public String getModDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddDoublePlayMod();
    }
}
