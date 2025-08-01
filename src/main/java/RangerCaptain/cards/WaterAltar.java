package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.OnUseEnergyPatches;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class WaterAltar extends AbstractEasyCard implements OnUseEnergyPatches.OnUseEnergyObject {
    public final static String ID = makeID(WaterAltar.class.getSimpleName());

    public WaterAltar() {
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
    public void onUseEnergy(int amount) {
        if (amount > 0 && Wiz.adp().hand.contains(this)) {
            superFlash();
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, Wiz.adp(), magicNumber)));
        }
    }

    @Override
    public void upp() {
        isEthereal = false;
        uDesc();
    }
}