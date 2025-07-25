package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.NextTurnBlockComponent;
import RangerCaptain.cardmods.fusion.components.ToxinComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static RangerCaptain.MainModfile.makeID;

public class Candevil extends AbstractMultiUpgradeCard implements StartupCard {
    public final static String ID = makeID(Candevil.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CANDEVIL)
                .withCost(1)
                .withBlock(4)
                .with(new NextTurnBlockComponent(4))
                .register();
        new FusionComponentHelper(MonsterEnum.MALCHEMY)
                .withCost(1)
                .withBlock(5)
                .with(new NextTurnBlockComponent(5))
                .with(new ToxinComponent(3))
                .register();
        new FusionComponentHelper(MonsterEnum.MIASMODEUS)
                .withCost(1)
                .withBlock(7)
                .with(new NextTurnBlockComponent(7))
                .with(new ToxinComponent(4))
                .register();
        new FusionComponentHelper(MonsterEnum.VENDEMON)
                .withCost(1)
                .withBlock(7)
                .with(new NextTurnBlockComponent(7))
                .register();
        new FusionComponentHelper(MonsterEnum.GUMBAAL)
                .withCost(1)
                .withBlock(10)
                .with(new NextTurnBlockComponent(10))
                .register();
    }

    public Candevil() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 5;
        setMonsterData(MonsterEnum.CANDEVIL);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToSelf(new NextTurnBlockPower(p, block));
        if (info == 1) {
            Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
        }
    }

    @Override
    public boolean atBattleStartPreDraw() {
        if (info == 2) {
            addToBot(new GainBlockAction(Wiz.adp(), magicNumber));
        }
        return false;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeBlock(1);
        if (baseMagicNumber < 0) {
            baseMagicNumber = magicNumber = 0;
        }
        upgradeMagicNumber(3);
        target = CardTarget.SELF_AND_ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MALCHEMY);
        baseInfo = info = 1;
        tags.add(CustomTags.MAGIC_TOXIN);
    }

    public void upgrade1() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MIASMODEUS);
    }

    public void upgrade2() {
        upgradeBlock(1);
        if (baseMagicNumber < 0) {
            baseMagicNumber = magicNumber = 0;
        }
        upgradeMagicNumber(6);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.VENDEMON);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.GUMBAAL);
    }
}