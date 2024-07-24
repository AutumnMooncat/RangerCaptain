package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.FormatHelper;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SummonedPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SummonedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractCard card;

    public SummonedPower(AbstractCreature owner, int amount, AbstractCard card) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        this.card = card.makeSameInstanceOf();
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (card == null) {
            description = "???";
        } else {
            if (amount == 1) {
                this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
            } else {
                this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(Integer.toString(amount), "#b") + DESCRIPTIONS[2] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
            }
        }
    }

    @Override
    public void onEnergyRecharge() {
        flash();
        addToBot(new MakeTempCardInHandAction(card, amount));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}