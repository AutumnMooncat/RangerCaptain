package RangerCaptain.cards;

import RangerCaptain.actions.ApplyPowerActionWithFollowup;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.NextTurnDamageComponent;
import RangerCaptain.cardfusion.components.TempStrengthComponent;
import RangerCaptain.cardfusion.components.VigorComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.NextTurnTakeDamagePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Bansheep extends AbstractMultiUpgradeCard implements StartupCard {
    public final static String ID = makeID(Bansheep.class.getSimpleName());

    static  {
        new FusionComponentHelper(MonsterEnum.BANSHEEP)
                .withCost(0)
                .withDamage(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new NextTurnDamageComponent(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .register();
        new FusionComponentHelper(MonsterEnum.WOOLTERGEIST)
                .withCost(0)
                .withDamage(6, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new NextTurnDamageComponent(6, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new TempStrengthComponent(3, AbstractComponent.ComponentTarget.ENEMY, false))
                .register();
        new FusionComponentHelper(MonsterEnum.RAMTASM)
                .withCost(0)
                .withDamage(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new NextTurnDamageComponent(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new TempStrengthComponent(4, AbstractComponent.ComponentTarget.ENEMY, false))
                .register();
        new FusionComponentHelper(MonsterEnum.ZOMBLEAT)
                .withCost(0)
                .withDamage(6, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new NextTurnDamageComponent(6, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VigorComponent(3))
                .register();
        new FusionComponentHelper(MonsterEnum.CAPRICORPSE)
                .withCost(0)
                .withDamage(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new NextTurnDamageComponent(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VigorComponent(4))
                .register();
    }

    public Bansheep() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 5;
        setMonsterData(MonsterEnum.BANSHEEP);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        Wiz.applyToEnemy(m, new NextTurnTakeDamagePower(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (info == 1) {
            addToBot(new ApplyPowerActionWithFollowup(
                    new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber)),
                    new ApplyPowerAction(m, p, new GainStrengthPower(m, magicNumber))
            ));
        }
    }

    @Override
    public boolean atBattleStartPreDraw() {
        if (info == 2) {
            Wiz.applyToSelf(new VigorPower(Wiz.adp(), magicNumber));
        }
        return false;
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
        upgradeDamage(1);
        if (baseMagicNumber < 0) {
            baseMagicNumber = magicNumber = 0;
        }
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WOOLTERGEIST);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.RAMTASM);
    }

    public void upgrade2() {
        upgradeDamage(1);
        if (baseMagicNumber < 0) {
            baseMagicNumber = magicNumber = 0;
        }
        upgradeMagicNumber(6);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.ZOMBLEAT);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.CAPRICORPSE);
    }
}