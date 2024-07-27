package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class EarthAltar extends AbstractEasyCard {
    public final static String ID = makeID(EarthAltar.class.getSimpleName());

    public EarthAltar() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExhaustAction(1, false, false, false));
        blck();
        addToBot(new ModifyBlockAction(uuid, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}