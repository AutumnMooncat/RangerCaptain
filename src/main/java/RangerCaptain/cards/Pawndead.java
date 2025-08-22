package RangerCaptain.cards;

import RangerCaptain.actions.StashCopyOfCardsAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.StashCardCopiesComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Pawndead extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Pawndead.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.PAWNDEAD)
                .withCost(2)
                .with(new StashCardCopiesComponent(1.5f))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.SKELEVANGELIST)
                .withCost(1)
                .with(new StashCardCopiesComponent(1.5f))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.KINGRAVE)
                .withCost(1)
                .with(new StashCardCopiesComponent(2.5f))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.QUEENYX)
                .withCost(0)
                .with(new StashCardCopiesComponent(1.5f))
                .withExhaust()
                .register();
    }

    public Pawndead() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.PAWNDEAD);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StashCopyOfCardsAction(Wiz.adp().hand, 1, magicNumber));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(darken(BLUE)), WHITE, darken(darken(BLUE)), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        setExclusions(1,2);
    }

    public void upgrade0() {
        upgradeBaseCost(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SKELEVANGELIST);
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.KINGRAVE);
    }

    public void upgrade2() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.QUEENYX);
    }
}