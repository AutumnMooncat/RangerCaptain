package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class MultitargetPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(MultitargetPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public MultitargetPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!card.purgeOnUse && card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
            if (card.type == AbstractCard.CardType.ATTACK && action.target != null) {
                flash();
                Wiz.forAllMonstersLiving(mon -> {
                    if (mon != action.target) {
                        AbstractCard tmp = card.makeSameInstanceOf();
                        AbstractDungeon.player.limbo.addToBottom(tmp);
                        tmp.current_x = card.current_x;
                        tmp.current_y = card.current_y;
                        tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                        tmp.target_y = Settings.HEIGHT / 2.0F;
                        tmp.calculateCardDamage(mon);

                        tmp.purgeOnUse = true;
                        AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, mon, card.energyOnUse, true, true), true);
                    }
                });
                addToTop(new ReducePowerAction(owner, owner, this, 1));
            }
        }
    }
}