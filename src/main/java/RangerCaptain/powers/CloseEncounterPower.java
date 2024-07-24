package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.FormatHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class CloseEncounterPower extends AbstractEasyPower implements NonStackablePower {
    public static final String POWER_ID = MainModfile.makeID(CloseEncounterPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractCard card;

    public CloseEncounterPower(AbstractCreature owner, AbstractCard card) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, -1);
        this.card = card;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (card == null) {
            description = "???";
        } else {
            this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onEnergyRecharge() {
        flash();
        addToBot(new DiscardToHandAction(card));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}