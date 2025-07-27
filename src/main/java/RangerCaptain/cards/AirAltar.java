package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class AirAltar extends AbstractEasyCard {
    public final static String ID = makeID(AirAltar.class.getSimpleName());

    public AirAltar() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseSecondMagic = secondMagic = 2;
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new MultitargetPower(p, secondMagic));
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToEnemyTop(m, new ResonancePower(m, p, magicNumber));
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}