package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.SureFireComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.SureFirePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Bulletino extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Bulletino.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BULLETINO)
                .withCost(1)
                .with(new DrawComponent(2.5f), new SureFireComponent(1.5f))
                .register();
        new FusionComponentHelper(MonsterEnum.VELOCIRIFLE)
                .withCost(1)
                .with(new DrawComponent(2.5f), new SureFireComponent(2.5f))
                .register();
        new FusionComponentHelper(MonsterEnum.ARTILLEREX)
                .withCost(1)
                .with(new DrawComponent(2.5f), new SureFireComponent(3.5f))
                .register();
        new FusionComponentHelper(MonsterEnum.GEARYU)
                .withCost(1)
                .with(new DrawComponent(3.5f), new SureFireComponent(2.5f))
                .register();
    }

    public Bulletino() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.BULLETINO);
        setElementalType(ElementalType.FIRE);
        tags.add(CustomTags.MAGIC_DRAW);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        Wiz.applyToSelf(new SureFirePower(p, secondMagic));
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
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        setExclusions(1,2);
    }

    public void upgrade0() {
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.VELOCIRIFLE);
    }

    public void upgrade1() {
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.ARTILLEREX);
    }

    public void upgrade2() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.GEARYU);
        setElementalType(ElementalType.METAL);
    }
}