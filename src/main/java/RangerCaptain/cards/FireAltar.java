package RangerCaptain.cards;

import RangerCaptain.actions.ModifyMagicAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class FireAltar extends AbstractEasyCard {
    public final static String ID = makeID(FireAltar.class.getSimpleName());

    public FireAltar() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 6;
        baseSecondMagic = secondMagic = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExhaustAction(1, false, false, false));
        Wiz.applyToSelf(new VigorPower(p, magicNumber));
        addToBot(new ModifyMagicAction(uuid, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        upgradeSecondMagic(1);
    }
}