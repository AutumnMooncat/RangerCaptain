package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.FormatHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CardToHandPower extends AbstractPower implements NonStackablePower {
    public static final String POWER_ID = MainModfile.makeID(CardToHandPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractCard card;

    public CardToHandPower(AbstractCreature owner, int amount, AbstractCard card) {
        this.ID = POWER_ID;
        this.name = CardModifierManager.onRenderTitle(card, card.name) + NAME;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.loadRegion("carddraw");
        this.card = card;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(Integer.toString(amount), "#b") + DESCRIPTIONS[2] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onEnergyRecharge() {
        flash();
        addToBot(new MakeTempCardInHandAction(card.makeSameInstanceOf(), amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}