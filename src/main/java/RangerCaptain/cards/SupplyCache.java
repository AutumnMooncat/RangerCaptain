package RangerCaptain.cards;

import RangerCaptain.actions.GatherAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class SupplyCache extends AbstractEasyCard {
    public final static String ID = makeID(SupplyCache.class.getSimpleName());

    public SupplyCache() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 7;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new GatherAction(1, c -> true, false, upgraded));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}