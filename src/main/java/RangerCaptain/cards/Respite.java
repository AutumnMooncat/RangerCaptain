package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ReboundPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Respite extends AbstractEasyCard {
    public final static String ID = makeID(Respite.class.getSimpleName());

    public Respite() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new VigorPower(p, magicNumber));
        Wiz.applyToSelf(new ReboundPower(p));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}