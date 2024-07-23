package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Busheye extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Busheye.class.getSimpleName());
    protected static Animation<TextureRegion> busheye = loadGifOverlay("Busheye_idle.gif");
    protected static Animation<TextureRegion> huntorch = loadGifOverlay("Huntorch_idle.gif");
    protected static Animation<TextureRegion> hedgeherne = loadGifOverlay("Hedgeherne_idle.gif");

    public Busheye() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        baseMagicNumber = magicNumber = 7;
        baseSecondMagic = secondMagic = 2;
        gifOverlay = busheye;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mon -> {
            Wiz.applyToEnemy(mon, new BurnedPower(mon, p, magicNumber));
            Wiz.applyToEnemy(mon, new WeakPower(mon, secondMagic, false));
        });
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
    }

    public void upgrade0() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = huntorch;
    }

    public void upgrade1() {
        upgradeMagicNumber(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = hedgeherne;
    }
}