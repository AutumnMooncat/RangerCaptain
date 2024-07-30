package RangerCaptain.cards;

import RangerCaptain.actions.CleansePowerAction;
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
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Padpole extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Padpole.class.getSimpleName());
    protected static Animation<TextureRegion> padpole = loadGifOverlay("Padpole_idle.gif");
    protected static Animation<TextureRegion> frillypad = loadGifOverlay("Frillypad_idle.gif");
    protected static Animation<TextureRegion> liligator = loadGifOverlay("Liligator_idle.gif");

    public Padpole() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 5;
        gifOverlay = padpole;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new CleansePowerAction(p, 1, power -> power.type == AbstractPower.PowerType.DEBUFF));
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
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = frillypad;
    }

    public void upgrade1() {
        upgradeBlock(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = liligator;
    }
}