package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class FireAltar extends AbstractEasyCard {
    public final static String ID = makeID(FireAltar.class.getSimpleName());

    public FireAltar() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 5;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BurnedPower(m, p, magicNumber));
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], false, true, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToSelfTop(new VigorPower(p, secondMagic));
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}