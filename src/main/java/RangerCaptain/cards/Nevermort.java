package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static RangerCaptain.MainModfile.makeID;

public class Nevermort extends AbstractEasyCard {
    public final static String ID = makeID(Nevermort.class.getSimpleName());
    protected static Animation<TextureRegion> nevermort = loadGifOverlay("Nevermort_idle.gif");
    protected static Animation<TextureRegion> apocrowlypse = loadGifOverlay("Apocrowlypse_idle.gif");

    public Nevermort() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 3;
        gifOverlay = nevermort;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new PoisonPower(mon, p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = apocrowlypse;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLACK, WHITE, BLACK, WHITE, false);
    }
}