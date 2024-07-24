package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.FormatHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class MindMeldPower extends AbstractEasyPower implements NonStackablePower {
    public static final String POWER_ID = MainModfile.makeID(MindMeldPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractCard card;

    public MindMeldPower(AbstractCreature owner, AbstractCard card) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, -1);
        this.name = NAME + " " + CardModifierManager.onRenderTitle(card, card.name);
        this.card = card;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (card == null) {
            this.description = "???";
        } else {
            this.description = DESCRIPTIONS[0] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(card, card.name), "#y") + DESCRIPTIONS[1];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse) {
            flash();

            AbstractCard tmp = this.card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, true, this.card.energyOnUse, true, true), true);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}