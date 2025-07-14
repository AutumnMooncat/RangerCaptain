package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ToxinComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Carniviper extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Carniviper.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CARNIVIPER)
                .withCost(1)
                .withDamage(4, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.MASQUERATTLE)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(3))
                .register();
        new FusionComponentHelper(MonsterEnum.AEROBOROS)
                .withCost(1)
                .withBlock(5)
                .withDamage(5, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(3))
                .register();
        new FusionComponentHelper(MonsterEnum.MARDIUSA)
                .withCost(2)
                .withDamage(3, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(2))
                .withDoublePlay()
                .register();
        new FusionComponentHelper(MonsterEnum.JORMUNGOLD)
                .withCost(0)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(3))
                .register();
    }

    public Carniviper() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.CARNIVIPER);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_TOXIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (info) {
            default:
                dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
                Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
                break;
            case 1:
                blck();
                dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
                Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
                break;
            case 2:
                dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
                Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
                break;
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        addUpgradeData(this::upgrade3, 0);
        setExclusions(1,2,3);
    }

    public void upgrade0() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MASQUERATTLE);
    }

    public void upgrade1() {
        if (baseBlock < 0) {
            baseBlock = 0;
        }
        upgradeBlock(7);
        upgradeDamage(-1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.AEROBOROS);
        baseInfo = info = 1;
    }

    public void upgrade2() {
        upgradeBaseCost(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.MARDIUSA);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.JORMUNGOLD);
        //baseInfo = info = 3;
    }
}