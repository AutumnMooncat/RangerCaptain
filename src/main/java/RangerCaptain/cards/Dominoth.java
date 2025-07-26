package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ConductiveComponent;
import RangerCaptain.cardmods.fusion.components.DrawComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Dominoth extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Dominoth.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DOMINOTH)
                .withCost(1)
                .with(new ConductiveComponent(3), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.WINGLOOM)
                .withCost(1)
                .with(new ConductiveComponent(5), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.MOTHMANIC)
                .withCost(1)
                .with(new ConductiveComponent(7), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.TOKUSECT)
                .withCost(1)
                .with(new ConductiveComponent(4), new DrawComponent(2))
                .register();
    }

    public Dominoth() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 4;
        setMonsterData(MonsterEnum.DOMINOTH);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_DRAW);
        tags.add(CustomTags.SECOND_MAGIC_CONDUCTIVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new ConductivePower(m, p, secondMagic));
        addToBot(new DrawCardAction(magicNumber));
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
        addUpgradeData(this::upgrade2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeSecondMagic(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WINGLOOM);
    }

    public void upgrade1() {
        upgradeSecondMagic(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MOTHMANIC);
    }

    public void upgrade2() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.TOKUSECT);
    }
}