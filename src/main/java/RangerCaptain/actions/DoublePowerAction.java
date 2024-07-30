package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractPowerCard;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DoublePowerAction extends AbstractGameAction {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("DoublePowerAction")).TEXT;
    private final Predicate<AbstractPower> filter;
    private final Consumer<ArrayList<AbstractPower>> callBack;

    public DoublePowerAction(AbstractCreature target, int amount, Predicate<AbstractPower> filter) {
        this(target, amount, filter, l -> {});
    }

    public DoublePowerAction(AbstractCreature target, int amount, Predicate<AbstractPower> filter, Consumer<ArrayList<AbstractPower>> callBack) {
        this.target = target;
        this.amount = amount;
        this.filter = filter.and(pow -> !(pow instanceof NonStackablePower) && pow.amount != 0);
        this.callBack = callBack;
    }


    @Override
    public void update() {
        if (amount <= 0 || target.powers.stream().noneMatch(filter)) {
            this.isDone = true;
            return;
        }
        if (amount >= target.powers.stream().filter(filter).count()) {
            ArrayList<AbstractPower> doubledPowers = new ArrayList<>();
            for (AbstractPower pow : target.powers) {
                if (filter.test(pow)) {
                    pow.stackPower(pow.amount);
                    pow.updateDescription();
                    doubledPowers.add(pow);
                }
            }
            callBack.accept(doubledPowers);
        } else {
            ArrayList<AbstractCard> validPowerCards = new ArrayList<>();
            HashMap<AbstractCard, AbstractPower> powerMap = new HashMap<>();
            for (AbstractPower pow : target.powers) {
                if (!(pow instanceof InvisiblePower) && pow.type != NeutralPowertypePatch.NEUTRAL && filter.test(pow)) {
                    AbstractCard card = new AbstractPowerCard(pow){};
                    validPowerCards.add(card);
                    powerMap.put(card, pow);
                }
            }
            Wiz.att(new BetterSelectCardsCenteredAction(validPowerCards, amount, amount == 1 ? TEXT[1] : TEXT[2] + amount + TEXT[3], cards -> {
                ArrayList<AbstractPower> doubledPowers = new ArrayList<>();
                for (AbstractCard card : cards) {
                    powerMap.get(card).stackPower(powerMap.get(card).amount);
                    powerMap.get(card).updateDescription();
                    doubledPowers.add(powerMap.get(card));
                }
                callBack.accept(doubledPowers);
            }));
        }
        this.isDone = true;
    }
}
