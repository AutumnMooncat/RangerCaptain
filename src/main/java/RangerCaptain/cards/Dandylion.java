package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsCenteredAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.unique.DiscardPileToTopOfDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Dandylion extends AbstractEasyCard {
    public final static String ID = makeID(Dandylion.class.getSimpleName());

    public Dandylion() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 7;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.DANDYLION);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new BetterSelectCardsCenteredAction(p.discardPile.group, magicNumber, DiscardPileToTopOfDeckAction.TEXT[0], false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                p.discardPile.removeCard(card);
                p.hand.moveToDeck(card, false);
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
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