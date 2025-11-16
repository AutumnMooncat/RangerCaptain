package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BlockComponent;
import RangerCaptain.cardfusion.components.ExhaustCardsComponent;
import RangerCaptain.cardfusion.components.ToxinComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Candevil extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Candevil.class.getSimpleName());

    static {
        // 2,4,2 -> 3,5,3
        new FusionComponentHelper(MonsterEnum.CANDEVIL)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new BlockComponent(2.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new ToxinComponent(1.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,4,3 -> 3,5,4
        new FusionComponentHelper(MonsterEnum.MALCHEMY)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new BlockComponent(2.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new ToxinComponent(2), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,4,4 -> 3,5,5
        new FusionComponentHelper(MonsterEnum.MIASMODEUS)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new BlockComponent(2.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new ToxinComponent(2.91f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,6,2 -> 3,7,3
        new FusionComponentHelper(MonsterEnum.VENDEMON)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new BlockComponent(3.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new ToxinComponent(1.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        // 2,8,2 -> 3,9,3
        new FusionComponentHelper(MonsterEnum.GUMBAAL)
                .withCost(1)
                .with(new ExhaustCardsComponent(1.91f))
                .withFlags(new BlockComponent(4.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .withFlags(new ToxinComponent(1.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
    }

    public Candevil() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 4;
        baseSecondMagic = secondMagic = 2;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.CANDEVIL);
        setElementalType(ElementalType.BEAST);
        tags.add(CustomTags.MAGIC_TOXIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(secondMagic, ExhaustAction.TEXT[0], false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToEnemyTop(m, new ToxinPower(m, magicNumber));
                blckTop();
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
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MALCHEMY);
        setElementalType(ElementalType.POISON);
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MIASMODEUS);
    }

    public void upgrade2() {
        upgradeBlock(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.VENDEMON);
        setElementalType(ElementalType.METAL);
    }

    public void upgrade3() {
        upgradeBlock(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.GUMBAAL);
    }
}