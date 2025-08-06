package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.NextTurnDamageComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.NextTurnDamagePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Springheel extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Springheel.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SPRINGHEEL)
                .withCost(1)
                .withBlock(5)
                .with(new NextTurnDamageComponent(3, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.HOPSKIN)
                .withCost(1)
                .withBlock(5)
                .with(new NextTurnDamageComponent(5, AbstractGameAction.AttackEffect.SLASH_DIAGONAL, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.RIPTERRA)
                .withCost(1)
                .withBlock(5)
                .with(new NextTurnDamageComponent(8, AbstractGameAction.AttackEffect.SLASH_HEAVY, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.SNOOPIN)
                .withCost(0)
                .withBlock(6)
                .with(new NextTurnDamageComponent(4, AbstractGameAction.AttackEffect.BLUNT_LIGHT, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.SCAMPIRE)
                .withCost(0)
                .withBlock(8)
                .with(new NextTurnDamageComponent(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
    }

    public Springheel() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseBlock = block = 6;
        baseDamage = damage = 4;
        isMultiDamage = true;
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
        for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
            AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
            if (!mon.isDeadOrEscaped()) {
                Wiz.applyToSelf(new NextTurnDamagePower(p, mon, new DamageInfo(p, multiDamage[i], damageTypeForTurn), fx));
            }
        }
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
        upgradeBlock(2);
        upgradeDamage(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.SNOOPIN);
    }

    public void upgrade3() {
        upgradeBlock(2);
        upgradeDamage(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.SCAMPIRE);
        baseInfo = info = 3;
    }
}