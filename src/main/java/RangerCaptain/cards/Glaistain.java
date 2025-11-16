package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.MakeCardsComponent;
import RangerCaptain.cardfusion.components.OnPerformFusionComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.GlassBondsPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Glaistain extends AbstractEasyCard {
    public final static String ID = makeID(Glaistain.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.GLAISTAIN)
                .withCost(0)
                .with(new OnPerformFusionComponent())
                .withFlags(new MakeCardsComponent(1.45f, new Glaistain(), false, MakeCardsComponent.Location.DISCARD), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Glaistain() {
        super(ID, 0, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.GLAISTAIN);
        setElementalType(ElementalType.GLASS);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new GlassBondsPower(p, magicNumber));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }
}