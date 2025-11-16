package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.SnowedInComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.SnowedInPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Spookionna extends AbstractEasyCard {
    public final static String ID = makeID(Spookionna.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SPOOKIONNA)
                .withCost(3)
                .with(new SnowedInComponent(1))
                .withExhaust()
                .register();
    }

    public Spookionna() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);
        setMonsterData(MonsterEnum.SPOOKIONNA);
        setElementalType(ElementalType.ICE);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new SnowedInPower(p, 1));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}