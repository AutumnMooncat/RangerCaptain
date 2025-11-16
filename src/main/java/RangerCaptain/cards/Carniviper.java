package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.ToxinComponent;
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
                .withDamage(4f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(3f))
                .register();
        new FusionComponentHelper(MonsterEnum.MASQUERATTLE)
                .withCost(1)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(4f))
                .register();
        new FusionComponentHelper(MonsterEnum.AEROBOROS)
                .withCost(0)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(4f))
                .register();
        new FusionComponentHelper(MonsterEnum.JORMUNGOLD)
                .withCost(1)
                .withDamage(7f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(7f))
                .register();
        new FusionComponentHelper(MonsterEnum.MARDIUSA)
                .withCost(2)
                .withMultiDamage(7f, 2, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new ToxinComponent(6f))
                .register();
    }

    public Carniviper() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 4;
        setMonsterData(MonsterEnum.CARNIVIPER);
        setElementalType(ElementalType.POISON);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_TOXIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
            dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
        } else {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
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
        upgradeDamage(2); // 8
        upgradeMagicNumber(1); // 5
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MASQUERATTLE);
    }

    public void upgrade1() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.AEROBOROS);
        setElementalType(ElementalType.AIR);
    }

    public void upgrade2() {
        upgradeDamage(1); // 9
        upgradeMagicNumber(4); // 9
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.JORMUNGOLD);
    }

    public void upgrade3() {
        upgradeBaseCost(2);
        upgradeDamage(2); // 10 x2
        upgradeMagicNumber(3); // 8
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.MARDIUSA);
        baseInfo = info = 1;
    }
}