package RangerCaptain.cards;

import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Camping extends AbstractEasyCard {
    public final static String ID = makeID(Camping.class.getSimpleName());

    public Camping() {
        super(ID, 2, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        baseBlock = block = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new StashCardsAction(Wiz.adp().hand, 1, false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                addToTop(new UpgradeSpecificCardAction(card));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(4);
    }
}