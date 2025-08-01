package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CardInHandSuite;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class AirAltar extends AbstractEasyCard implements CardInHandSuite.InHandCard {
    public final static String ID = makeID(AirAltar.class.getSimpleName());

    public AirAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 1;
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onCardExhausted(AbstractCard card) {
        if (card != this) {
            superFlash();
            addToBot(new ApplyPowerToRandomEnemyAction(Wiz.adp(), new ResonancePower(null, Wiz.adp(), magicNumber), magicNumber));
        }
    }

    @Override
    public void upp() {
        isEthereal = false;
        uDesc();
    }
}