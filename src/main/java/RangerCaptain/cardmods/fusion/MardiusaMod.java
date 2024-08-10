package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MardiusaMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(MardiusaMod.class.getSimpleName());
    public static final String DESCRIPTION = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;

    public MardiusaMod() {
        super(ID, DESCRIPTION, CARD_TEXT[0]);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.cost >= 0) {
            card.cost++;
            card.costForTurn++;
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        AbstractCard copy = card.makeStatEquivalentCopy();
        CardModifierManager.removeModifiersById(copy, ID, false);
        GameActionManager.queueExtraCard(copy, (AbstractMonster) target);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MardiusaMod();
    }
}
