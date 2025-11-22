package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class InterceptingPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(InterceptingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean justEvoked = true;
    public boolean primed = false;

    public InterceptingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
        // This is also extremely jank
        MainModfile.safeEffectQueue.add(new AbstractGameEffect() {
            @Override
            public void update() {
                this.isDone = true;
                if (AbstractDungeon.actionManager.actions.stream().noneMatch(a -> a instanceof UseCardAction)) {
                    justEvoked = false;
                }
            }

            @Override
            public void render(SpriteBatch spriteBatch) {}

            @Override
            public void dispose() {}
        });
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
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (justEvoked) {
            justEvoked = false; // TODO fails to work when given by power
        } else {
            primed = true;
            if (card.type != AbstractCard.CardType.POWER) {
                flash();
            }
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}