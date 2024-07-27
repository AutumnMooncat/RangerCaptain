package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.FlammablePower;
import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Salamagus extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Salamagus.class.getSimpleName());
    protected static Animation<TextureRegion> salamagus = loadGifOverlay("Salamagus_idle.gif");
    protected static Animation<TextureRegion> adeptile = loadGifOverlay("Adeptile_idle.gif");
    protected static Animation<TextureRegion> pyromeleon = loadGifOverlay("Pyromeleon_idle.gif");

    public Salamagus() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        gifOverlay = salamagus;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new FlammablePower(p, magicNumber));
        if (info == 1) {
            Salamagus copy = (Salamagus) makeStatEquivalentCopy();
            copy.baseInfo = copy.info = -1;
            Wiz.applyToSelf(new MindMeldPower(p, copy));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeMagicNumber(-1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = adeptile;
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = pyromeleon;
    }
}