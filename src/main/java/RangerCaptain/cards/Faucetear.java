package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.DiscardCardsComponent;
import RangerCaptain.cardmods.fusion.components.DiscardToHandComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.common.BetterDiscardPileToHandAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Faucetear extends AbstractEasyCard {
    public final static String ID = makeID(Faucetear.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.FAUCETEAR)
                .withCost(1)
                .with(new DiscardCardsComponent(2, true, false))
                .withFlags(new DiscardToHandComponent(0), AbstractComponent.Flag.THAT_MANY)
                .register();
        new FusionComponentHelper(MonsterEnum.FOUNTESS)
                .withCost(1)
                .with(new DiscardCardsComponent(3, true, false))
                .withFlags(new DiscardToHandComponent(0), AbstractComponent.Flag.THAT_MANY)
                .register();
    }

    public Faucetear() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.FAUCETEAR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(magicNumber, DiscardAction.TEXT[0], true, true, c -> true, cards -> {
            if (!cards.isEmpty()) {
                addToTop(new BetterDiscardPileToHandAction(cards.size()));
            }
            for (AbstractCard card : cards) {
                addToTop(new DiscardSpecificCardAction(card, p.hand));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FOUNTESS);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }
}