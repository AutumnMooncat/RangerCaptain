package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.MakeCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Minortom extends AbstractEasyCard {
    public final static String ID = makeID(Minortom.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MINORTOM)
                .withCost(0)
                .with(new DrawComponent(1.91f))
                .with(new MakeCardsComponent(0.91f, new VoidCard(), false, MakeCardsComponent.Location.DISCARD))
                .register();
        new FusionComponentHelper(MonsterEnum.MAJORTOM)
                .withCost(0)
                .with(new DrawComponent(2.91f))
                .with(new MakeCardsComponent(0.91f, new VoidCard(), false, MakeCardsComponent.Location.DISCARD))
                .register();
    }

    public Minortom() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.MINORTOM);
        cardsToPreview = new VoidCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new VoidCard(), 1));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MAJORTOM);
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