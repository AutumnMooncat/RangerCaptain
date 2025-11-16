package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
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
        // 6,6 -> 8,8
        new FusionComponentHelper(MonsterEnum.BANSHEEP)
                .withCost(1)
                .withDamage(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new VigorComponent(4), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 6,9 -> 8,13
        new FusionComponentHelper(MonsterEnum.WOOLTERGEIST)
                .withCost(1)
                .withDamage(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new VigorComponent(6.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 6,12 -> 8,18
        new FusionComponentHelper(MonsterEnum.RAMTASM)
                .withCost(1)
                .withDamage(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new VigorComponent(9), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 9,6 -> 13,8
        new FusionComponentHelper(MonsterEnum.ZOMBLEAT)
                .withCost(1)
                .withDamage(6.5f, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new VigorComponent(4), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 12,6 -> 18,8
        new FusionComponentHelper(MonsterEnum.CAPRICORPSE)
                .withCost(1)
                .withDamage(9, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new VigorComponent(4), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
    }

    public Bansheep() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 6;
        setMonsterData(MonsterEnum.BANSHEEP);
        setElementalType(ElementalType.BEAST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], true, true, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToSelfTop(new VigorPower(p, magicNumber));
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
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WOOLTERGEIST);
        setElementalType(ElementalType.ASTRAL);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.RAMTASM);
    }

    public void upgrade2() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.ZOMBLEAT);
        setElementalType(ElementalType.EARTH);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.CAPRICORPSE);
    }
}