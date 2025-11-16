package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.BoobyTrapComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.BoobyTrappedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Charlequin extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Charlequin.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CHARLEQUIN)
                .withCost(2)
                .with(new BoobyTrapComponent(1))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.BLUNDERBUSK)
                .withCost(1)
                .with(new BoobyTrapComponent(1))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.FRAGLIACCI)
                .withCost(3)
                .with(new BoobyTrapComponent(2))
                .withExhaust()
                .register();
    }

    public Charlequin() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        setMonsterData(MonsterEnum.CHARLEQUIN);
        setElementalType(ElementalType.FIRE);
        baseSecondMagic = secondMagic = BoobyTrappedPower.BOOBY_TRAP_DAMAGE;
        baseMagicNumber = magicNumber = 1;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BoobyTrappedPower(m, magicNumber));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(Color.PINK), WHITE, darken(Color.PINK), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0,1);
    }

    public void upgrade0() {
        upgradeBaseCost(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.BLUNDERBUSK);
    }

    public void upgrade1() {
        upgradeBaseCost(3);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.FRAGLIACCI);
    }
}