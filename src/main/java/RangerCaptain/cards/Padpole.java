package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.powers.LeechedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Padpole extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Padpole.class.getSimpleName());
    protected static Animation<TextureRegion> padpole = loadGifOverlay("Padpole_idle.gif");
    protected static Animation<TextureRegion> frillypad = loadGifOverlay("Frillypad_idle.gif");
    protected static Animation<TextureRegion> liligator = loadGifOverlay("Liligator_idle.gif");

    public Padpole() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 4;
        gifOverlay = padpole;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new LeechedPower(m, p, magicNumber));
        Wiz.applyToSelf(new APBoostPower(p, 1));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
    }

    public void upgrade0() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = frillypad;
    }

    public void upgrade1() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = liligator;
    }
}