package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Amphare extends AbstractEasyCard {
    public final static String ID = makeID(Amphare.class.getSimpleName());
    protected static Animation<TextureRegion> amphare = loadGifOverlay("Amphare_idle.gif");
    protected static Animation<TextureRegion> lapacitor = loadGifOverlay("Lapacitor_idle.gif");

    public Amphare() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        gifOverlay = amphare;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new APBoostPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = lapacitor;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}