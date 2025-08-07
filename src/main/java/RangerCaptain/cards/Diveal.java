package RangerCaptain.cards;

import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.actions.StashRandomCardsAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.StashCardsComponent;
import RangerCaptain.cardmods.fusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Diveal extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Diveal.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DIVEAL)
                .withCost(1)
                .with(new WeakComponent(1))
                .with(new StashCardsComponent(2, StashCardsComponent.TargetPile.DISCARD, false, true))
                .register();
        new FusionComponentHelper(MonsterEnum.DIVEBERG)
                .withCost(1)
                .with(new WeakComponent(1))
                .with(new StashCardsComponent(2, StashCardsComponent.TargetPile.DISCARD, true, false))
                .register();
        new FusionComponentHelper(MonsterEnum.SCUBALRUS)
                .withCost(1)
                .with(new WeakComponent(1))
                .with(new StashCardsComponent(3, StashCardsComponent.TargetPile.DISCARD, false, true))
                .register();
    }

    public Diveal() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.DIVEAL);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new WeakPower(m, secondMagic, false));
        if (info == 1) {
            addToBot(new StashCardsAction(Wiz.adp().discardPile, magicNumber, true, true));
        } else {
            addToBot(new StashRandomCardsAction(Wiz.adp().discardPile, magicNumber));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        //upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.DIVEBERG);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.SCUBALRUS);
    }
}