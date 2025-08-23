package RangerCaptain.cards;

import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.StashCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Dandylion extends AbstractEasyCard {
    public final static String ID = makeID(Dandylion.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DANDYLION)
                .withCost(1)
                .with(new DrawComponent(1.5f))
                .with(new StashCardsComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.BLOSSOMAW)
                .withCost(1)
                .with(new DrawComponent(2.5f))
                .with(new StashCardsComponent(1))
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
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new StashCardsAction(Wiz.adp().hand, secondMagic, false, false));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
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