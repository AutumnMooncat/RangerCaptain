package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.SharpenComponent;
import RangerCaptain.cardfusion.components.VigorComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.SharpenPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Squirey extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Squirey.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SQUIREY)
                .withCost(1)
                .with(new VigorComponent(4), new SharpenComponent(0.75f))
                .register();
        new FusionComponentHelper(MonsterEnum.MANISPEAR)
                .withCost(2)
                .with(new VigorComponent(4), new SharpenComponent(1.91f))
                .register();
        new FusionComponentHelper(MonsterEnum.PALANGOLIN)
                .withCost(1)
                .with(new VigorComponent(6), new SharpenComponent(0.75f))
                .register();
    }

    public Squirey() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.SQUIREY);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new VigorPower(p, magicNumber));
        Wiz.applyToSelf(new SharpenPower(p, secondMagic));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BRONZE, WHITE, BRONZE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBaseCost(2);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MANISPEAR);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PALANGOLIN);
        baseInfo = info = 2;
    }
}