package RangerCaptain.cards;

import RangerCaptain.actions.StashTopCardsAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class FlashFlood extends AbstractEasyCard {
    public final static String ID = makeID(FlashFlood.class.getSimpleName());

    public FlashFlood() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StashTopCardsAction(magicNumber, cards -> {
            for (AbstractCard card : cards) {
                card.freeToPlayOnce = true;
            }
        }));
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (Wiz.adp().drawPile.size() < magicNumber) {
            glowColor = Settings.RED_TEXT_COLOR.cpy();
        }
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}