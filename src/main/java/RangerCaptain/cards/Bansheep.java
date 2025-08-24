package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.DamageComponent;
import RangerCaptain.cardfusion.components.ExhaustCardsComponent;
import RangerCaptain.cardfusion.components.VigorComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Bansheep extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Bansheep.class.getSimpleName());

    static  {
        // 2,5,3 -> 3,6,4
        new FusionComponentHelper(MonsterEnum.BANSHEEP)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new DamageComponent(3, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new VigorComponent(2), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,5,4 -> 3,6,5
        new FusionComponentHelper(MonsterEnum.WOOLTERGEIST)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new DamageComponent(3, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new VigorComponent(2.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,5,5 -> 3,6,6
        new FusionComponentHelper(MonsterEnum.RAMTASM)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new DamageComponent(3, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new VigorComponent(3), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,7,3 -> 3,8,4
        new FusionComponentHelper(MonsterEnum.ZOMBLEAT)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new DamageComponent(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new VigorComponent(2), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,9,3 -> 3,10,4
        new FusionComponentHelper(MonsterEnum.CAPRICORPSE)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new DamageComponent(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new VigorComponent(2), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
    }

    public Bansheep() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.BANSHEEP);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(secondMagic, ExhaustAction.TEXT[0], false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToSelfTop(new VigorPower(p, magicNumber));
                dmgTop(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
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
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WOOLTERGEIST);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.RAMTASM);
    }

    public void upgrade2() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.ZOMBLEAT);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.CAPRICORPSE);
    }
}