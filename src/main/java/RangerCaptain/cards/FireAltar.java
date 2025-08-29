package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.interfaces.ManuallySizeAdjustedCard;
import RangerCaptain.cards.interfaces.OnCreateCardCard;
import RangerCaptain.patches.CardInHandSuite;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class FireAltar extends AbstractEasyCard implements CardInHandSuite.InHandCard, OnCreateCardCard, ManuallySizeAdjustedCard {
    public final static String ID = makeID(FireAltar.class.getSimpleName());

    public FireAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    private void onTrigger() {
        superFlash();
        Wiz.applyToSelf(new VigorPower(Wiz.adp(), magicNumber));
    }

    @Override
    public void onCardExhausted(AbstractCard card) {
        onTrigger();
    }

    @Override
    public void onCreateCard(AbstractCard card) {
        if (upgraded) {
            onTrigger();
        }
    }

    @Override
    public float getAdjustedScale() {
        return 0.97f;
    }

    @Override
    public void upp() {
        uDesc();
    }
}