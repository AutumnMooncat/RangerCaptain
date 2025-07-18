package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ConductiveComponent;
import RangerCaptain.cardmods.fusion.components.DrawComponent;
import RangerCaptain.cardmods.fusion.components.VigorComponent;
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
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Dominoth extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Dominoth.class.getSimpleName());

    static {
        // TODO "for each" modifier
        new FusionComponentHelper(MonsterEnum.DOMINOTH)
                .withCost(1)
                .with(new VigorComponent(4), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.WINGLOOM)
                .withCost(1)
                .with(new ConductiveComponent(4), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.MOTHMANIC)
                .withCost(1)
                .with(new ConductiveComponent(6), new DrawComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.TOKUSECT)
                .withCost(1)
                .with(new VigorComponent(4), new DrawComponent(2))
                .register();
    }

    public Dominoth() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 0;
        setMonsterData(MonsterEnum.DOMINOTH);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_DRAW);
        tags.add(CustomTags.SECOND_MAGIC_VIGOR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            Wiz.applyToEnemy(m, new ConductivePower(m, p, (int) (secondMagic + Wiz.adp().hand.group.stream().filter(c -> c != this).count())));
        }
        if (info == 0) {
            Wiz.applyToSelf(new VigorPower(p, (int) (secondMagic + Wiz.adp().hand.group.stream().filter(c -> c != this).count())));
        }
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
        // TODO Vigor -> Conductive with no other upgrades?
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WINGLOOM);
        baseInfo = info = 1;
        target = CardTarget.ENEMY;
        tags.add(CustomTags.SECOND_MAGIC_CONDUCTIVE);
        tags.remove(CustomTags.SECOND_MAGIC_VIGOR);
    }

    public void upgrade1() {
        upgradeSecondMagic(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MOTHMANIC);
    }

    public void upgrade2() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.TOKUSECT);
    }
}