package RangerCaptain.actions;

import RangerCaptain.cardmods.AbstractInfusion;
import RangerCaptain.powers.interfaces.OnCreateInfusionPower;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

public class InfuseCardsInHandAction extends ModifyCardsInHandAction {
    public static Predicate<AbstractCard> shenaniganFilter = c -> AbstractInfusion.usesVanillaTargeting(c) && c.costForTurn > -2;
    public InfuseCardsInHandAction(int amount, AbstractCardModifier mod) {
        this(amount, false, c -> true, mod);
    }

    public InfuseCardsInHandAction(int amount, boolean anyAmount, Predicate<AbstractCard> filter, AbstractCardModifier mod) {
        super(amount, anyAmount, filter.and(shenaniganFilter), l -> {
            for (AbstractCard c : l) {
                doInfusion(c, mod);
            }
            infusionSFX();
        });
    }

    public static void doInfusion(AbstractCard c, AbstractCardModifier mod) {
        int times = 1;
        for (AbstractPower p : Wiz.adp().powers) {
            if (p instanceof OnCreateInfusionPower) {
                times += ((OnCreateInfusionPower) p).increaseTimes(c, mod);
            }
        }
        for (int i = 0 ; i < times ; i++) {
            CardModifierManager.addModifier(c, mod.makeCopy());
            //Wiz.att(new ApplyCardModifierAction(c, mod.makeCopy()));
        }
        c.superFlash();
    }

    public static void infusionSFX() {
        CardCrawlGame.sound.play("MONSTER_COLLECTOR_SUMMON", 0.2f);
        CardCrawlGame.sound.play("RELIC_DROP_MAGICAL", 0.2f);
    }
}
