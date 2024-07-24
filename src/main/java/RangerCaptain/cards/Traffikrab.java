package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import basemod.devcommands.draw.Draw;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import static RangerCaptain.MainModfile.makeID;

public class Traffikrab extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Traffikrab.class.getSimpleName());
    protected static Animation<TextureRegion> traffikrab = loadGifOverlay("Traffikrab_idle.gif");
    protected static Animation<TextureRegion> weevilite = loadGifOverlay("Weevilite_idle.gif");
    protected static Animation<TextureRegion> lobstacle = loadGifOverlay("Lobstacle_idle.gif");

    public Traffikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 7;
        baseMagicNumber = magicNumber = 1;
        gifOverlay = traffikrab;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToSelf(new DrawCardNextTurnPower(p, magicNumber));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, ORANGE, WHITE, ORANGE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBaseCost(0);
        upgradeBlock(-2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = weevilite;
    }

    public void upgrade1() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = lobstacle;
    }
}