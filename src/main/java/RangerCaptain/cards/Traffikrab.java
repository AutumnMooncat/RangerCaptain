package RangerCaptain.cards;

import RangerCaptain.actions.StashTopCardsAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.StashTopCardsComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Traffikrab extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Traffikrab.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.TRAFFIKRAB)
                .withCost(1)
                .withBlock(5)
                .with(new StashTopCardsComponent(0.91f))
                .register();
        new FusionComponentHelper(MonsterEnum.WEEVILITE)
                .withCost(0)
                .withBlock(4)
                .with(new StashTopCardsComponent(0.91f))
                .register();
        new FusionComponentHelper(MonsterEnum.LOBSTACLE)
                .withCost(1)
                .withBlock(7.5f)
                .with(new StashTopCardsComponent(0.91f))
                .register();
    }

    public Traffikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 7;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.TRAFFIKRAB);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new StashTopCardsAction(magicNumber));
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
        upgradeBaseCost(0);
        upgradeBlock(-2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WEEVILITE);
    }

    public void upgrade1() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.LOBSTACLE);
    }
}