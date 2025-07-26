package RangerCaptain.cards;

import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.NextTurnEnergyComponent;
import RangerCaptain.cardmods.fusion.components.StashCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Dandylion extends AbstractEasyCard {
    public final static String ID = makeID(Dandylion.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DANDYLION)
                .withCost(1)
                .with(new StashCardsComponent(2, StashCardsComponent.TargetPile.HAND, true, false))
                .with(new NextTurnEnergyComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.BLOSSOMAW)
                .withCost(1)
                .with(new StashCardsComponent(2, StashCardsComponent.TargetPile.HAND, true, false))
                .with(new NextTurnEnergyComponent(2))
                .register();
    }

    public Dandylion() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.DANDYLION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StashCardsAction(Wiz.adp().hand, magicNumber, true, true));
        Wiz.applyToSelf(new APBoostPower(p, secondMagic));
    }

    @Override
    public void upp() {
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.BLOSSOMAW);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }
}