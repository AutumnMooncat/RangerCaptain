package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class WaterAltar extends AbstractEasyCard {
    public final static String ID = makeID(WaterAltar.class.getSimpleName());

    public WaterAltar() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new ConductivePower(m, p, magicNumber));
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], false, true, c -> true, cards -> {
            for (AbstractCard card : cards) {
                addToBot(new GainEnergyAction(secondMagic));
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}