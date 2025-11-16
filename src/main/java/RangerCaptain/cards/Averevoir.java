package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BlockComponent;
import RangerCaptain.cardfusion.components.OnPlayPerfectComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.TowerDefencePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Averevoir extends AbstractEasyCard {
    public final static String ID = makeID(Averevoir.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.AVEREVOIR)
                .withCost(2)
                .with(new OnPlayPerfectComponent())
                .withFlags(new BlockComponent(12.5f), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Averevoir() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        setMonsterData(MonsterEnum.AVEREVOIR);
        setElementalType(ElementalType.AIR);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new TowerDefencePower(p, magicNumber));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }
}