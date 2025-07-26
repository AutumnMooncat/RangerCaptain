package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Respite extends AbstractEasyCard {
    public final static String ID = makeID(Respite.class.getSimpleName());

    public Respite() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 0;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new VigorPower(p, (int) (magicNumber + Wiz.adp().hand.group.stream().filter(c -> c != this).count())));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        baseInfo = info = (int) (magicNumber + Wiz.adp().hand.group.stream().filter(c -> c != this).count());
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
    }
}