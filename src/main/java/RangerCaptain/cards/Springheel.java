package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.NextTurnDamageComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.NextTurnTakeDamagePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Springheel extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Springheel.class.getSimpleName());

    static {
        // 6,6 -> 9,9
        new FusionComponentHelper(MonsterEnum.SPRINGHEEL)
                .withCost(1)
                .withBlock(4.5f)
                .with(new NextTurnDamageComponent(4.5f, AbstractGameAction.AttackEffect.BLUNT_LIGHT))
                .register();
        // 6,9 -> 9,13
        new FusionComponentHelper(MonsterEnum.HOPSKIN)
                .withCost(1)
                .withBlock(4.5f)
                .with(new NextTurnDamageComponent(6.5f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL))
                .register();
        // 6, 12 -> 9,18
        new FusionComponentHelper(MonsterEnum.RIPTERRA)
                .withCost(1)
                .withBlock(4.5f)
                .with(new NextTurnDamageComponent(9, AbstractGameAction.AttackEffect.SLASH_HEAVY))
                .register();
        // 9,6 -> 13,9
        new FusionComponentHelper(MonsterEnum.SNOOPIN)
                .withCost(1)
                .withBlock(6.5f)
                .with(new NextTurnDamageComponent(4.5f, AbstractGameAction.AttackEffect.BLUNT_LIGHT))
                .register();
        // 12,6 -> 18,9
        new FusionComponentHelper(MonsterEnum.SCAMPIRE)
                .withCost(1)
                .withBlock(9)
                .with(new NextTurnDamageComponent(4.5f, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .register();
    }

    public Springheel() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 6;
        baseDamage = damage = 6;
        setMonsterData(MonsterEnum.SPRINGHEEL);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        AbstractGameAction.AttackEffect fx = AbstractGameAction.AttackEffect.BLUNT_LIGHT;
        if (info == 1) {
            fx = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
        } else if (info == 2) {
            fx = AbstractGameAction.AttackEffect.SLASH_HEAVY;
        } else if (info == 3) {
            fx = AbstractGameAction.AttackEffect.BLUNT_HEAVY;
        }
        Wiz.applyToEnemy(m, new NextTurnTakeDamagePower(m, new DamageInfo(p, damage, damageTypeForTurn), fx));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(IRIS), WHITE, darken(IRIS), WHITE, false);
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
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.HOPSKIN);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.RIPTERRA);
        baseInfo = info = 2;
    }

    public void upgrade2() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.SNOOPIN);
    }

    public void upgrade3() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.SCAMPIRE);
        baseInfo = info = 3;
    }
}