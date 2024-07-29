package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.CustomIntentPatches;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.lang.reflect.Field;

public class SnowedInPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SnowedInPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SnowedInPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, true, owner, amount);
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
    public boolean canPlayCard(AbstractCard card) {
        return card.type != AbstractCard.CardType.ATTACK;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    @Override
    public void onInitialApplication() {
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                Wiz.forAllMonstersLiving(mon -> {
                    if (mon.getIntentBaseDmg() > -1) {
                        try {
                            Field f = AbstractMonster.class.getDeclaredField("move");
                            f.setAccessible(true);
                            EnemyMoveInfo stunMove = new EnemyMoveInfo(mon.nextMove, CustomIntentPatches.RANGER_SNOWED_IN, -1, 0, false);
                            f.set(mon, stunMove);
                            mon.createIntent();
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
                this.isDone = true;
            }
        });
    }


}