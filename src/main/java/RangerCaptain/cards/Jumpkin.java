package RangerCaptain.cards;

import RangerCaptain.actions.CleansePowerAction;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
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

public class Jumpkin extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Jumpkin.class.getSimpleName());
    protected static Animation<TextureRegion> jumpkin = loadGifOverlay("Jumpkin_idle.gif");
    protected static Animation<TextureRegion> draculeaf = loadGifOverlay("Draculeaf_idle.gif");
    protected static Animation<TextureRegion> beanstalker = loadGifOverlay("Beanstalker_idle.gif");

    public Jumpkin() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 5;
        gifOverlay = jumpkin;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            blck();
        } else if (info == 1) {
            Wiz.applyToEnemy(m, new LeechedPower(m, p, magicNumber));
        }
        addToBot(new CleansePowerAction(p, 1, power -> power.type == AbstractPower.PowerType.DEBUFF));
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
        baseMagicNumber = magicNumber = 0;
        upgradeMagicNumber(4);
        target = CardTarget.ENEMY;
        baseBlock = block = -1;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = draculeaf;
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = beanstalker;
    }
}