package RangerCaptain.cards;

import RangerCaptain.actions.ModifyMagicAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class WaterAltar extends AbstractEasyCard {
    public final static String ID = makeID(WaterAltar.class.getSimpleName());

    public WaterAltar() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExhaustAction(1, false, false, false));
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new ModifyMagicAction(uuid, 1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}