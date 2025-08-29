package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CardInHandSuite;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class EarthAltar extends AbstractEasyCard implements CardInHandSuite.InHandCard {
    public final static String ID = makeID(EarthAltar.class.getSimpleName());

    public EarthAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onCardUsed(AbstractCard card) {
        if (card.type == CardType.ATTACK || upgraded) {
            superFlash();
            addToBot(new GainBlockAction(Wiz.adp(), magicNumber));
        }
    }

    @Override
    public void upp() {
        uDesc();
    }
}