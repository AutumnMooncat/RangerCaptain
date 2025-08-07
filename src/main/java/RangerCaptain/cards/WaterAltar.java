package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.OnChangeEnergyPatches;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static RangerCaptain.MainModfile.makeID;

public class WaterAltar extends AbstractEasyCard implements OnChangeEnergyPatches.OnChangeEnergyObject {
    public final static String ID = makeID(WaterAltar.class.getSimpleName());

    public WaterAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public int onUseEnergy(int amount) {
        if (amount > 0 && Wiz.adp().hand.contains(this)) {
            superFlash();
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, Wiz.adp(), magicNumber)));
        }
        return amount;
    }

    @Override
    public int onGainEnergy(int amount) {
        if (upgraded && amount > 0 && Wiz.adp().hand.contains(this)) {
            superFlash();
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, Wiz.adp(), magicNumber)));
        }
        return amount;
    }

    @Override
    public int onSetEnergy(int amount) {
        if (upgraded && amount > EnergyPanel.totalCount && Wiz.adp().hand.contains(this)) {
            superFlash();
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ConductivePower(mon, Wiz.adp(), magicNumber)));
        }
        return amount;
    }

    @Override
    public void upp() {
        uDesc();
    }
}