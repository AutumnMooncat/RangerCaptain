package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.NextTwoTurnsEnergyComponent;
import RangerCaptain.cardfusion.components.ToxinComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.powers.NextTurnPowerLaterPower;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Jumpkin extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Jumpkin.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.JUMPKIN)
                .withCost(2)
                .with(new ToxinComponent(7f))
                .with(new NextTwoTurnsEnergyComponent(1))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.DRACULEAF)
                .withCost(1)
                .with(new ToxinComponent(7f))
                .with(new NextTwoTurnsEnergyComponent(1))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.BEANSTALKER)
                .withCost(2)
                .with(new ToxinComponent(10f))
                .with(new NextTwoTurnsEnergyComponent(1))
                .withExhaust()
                .register();
    }

    public Jumpkin() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 8;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.JUMPKIN);
        tags.add(CustomTags.MAGIC_TOXIN);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
        Wiz.applyToSelf(new APBoostPower(p, secondMagic));
        Wiz.applyToSelf(new NextTurnPowerLaterPower(p, new APBoostPower(p, secondMagic)));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, ORANGE, WHITE, ORANGE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBaseCost(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.DRACULEAF);
    }

    public void upgrade1() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.BEANSTALKER);
    }
}