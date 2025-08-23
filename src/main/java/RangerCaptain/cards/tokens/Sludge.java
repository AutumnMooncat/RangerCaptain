package RangerCaptain.cards.tokens;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Sludge extends AbstractEasyCard {
    public final static String ID = makeID(Sludge.class.getSimpleName());

    public Sludge() {
        super(ID, 0, CardType.STATUS, CardRarity.COMMON, CardTarget.SELF, CardColor.COLORLESS);
        magicNumber = baseMagicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new ToxinPower(p, magicNumber));
    }

    @Override
    public void upp() {}
}