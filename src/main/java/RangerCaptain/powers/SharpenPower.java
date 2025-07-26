package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class SharpenPower extends AbstractEasyPower {
    public static final String POWER_ID = MainModfile.makeID(SharpenPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public SharpenPower(AbstractCreature owner, int amount) {
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
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    @SpirePatch2(clz = VigorPower.class, method = "onUseCard")
    public static class VigorPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> plz(VigorPower __instance, AbstractCard card) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                AbstractPower sharpen = __instance.owner.getPower(SharpenPower.POWER_ID);
                if (sharpen != null) {
                    sharpen.flash();
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }
}