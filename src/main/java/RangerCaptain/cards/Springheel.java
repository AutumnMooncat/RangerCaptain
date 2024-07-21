package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Springheel extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Springheel.class.getSimpleName());
    protected static Animation<TextureRegion> springheel = loadGifOverlay("Springheel_idle.gif");
    protected static Animation<TextureRegion> hopskin = loadGifOverlay("Hopskin_idle.gif");
    protected static Animation<TextureRegion> riptrrra = loadGifOverlay("Ripterra_idle.gif");
    protected static Animation<TextureRegion> snoopin = loadGifOverlay("Snoopin_idle.gif");
    protected static Animation<TextureRegion> scampire = loadGifOverlay("Scampire_idle.gif");

    public Springheel() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 1;
        gifOverlay = springheel;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        Wiz.applyToEnemy(m, new WeakPower(m, magicNumber, false));
        if (info > 0) {
            Wiz.applyToSelf(new EnergizedBluePower(p, info));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(IRIS), WHITE, darken(IRIS), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = hopskin;
    }

    public void upgrade1() {
        upgradeDamage(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = riptrrra;
    }

    public void upgrade2() {
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        gifOverlay = snoopin;
        baseInfo = info = 1;
    }

    public void upgrade3() {
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        gifOverlay = scampire;
        baseInfo = info = 2;
    }
}