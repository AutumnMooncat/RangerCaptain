package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static RangerCaptain.MainModfile.makeID;

public class Candevil extends AbstractMultiUpgradeCard implements StartupCard {
    public final static String ID = makeID(Candevil.class.getSimpleName());
    protected static Animation<TextureRegion> candevil = loadGifOverlay("Candevil_idle.gif");
    protected static Animation<TextureRegion> malchemy = loadGifOverlay("Malchemy_idle.gif");
    protected static Animation<TextureRegion> miasmodeus = loadGifOverlay("Miasmodeus_idle.gif");
    protected static Animation<TextureRegion> vendemon = loadGifOverlay("Vendemon_idle.gif");
    protected static Animation<TextureRegion> gumbaal = loadGifOverlay("Gumbaal_idle.gif");

    public Candevil() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 9;
        baseMagicNumber = magicNumber = 0;
        gifOverlay = candevil;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (info == 1) {
            Wiz.applyToEnemy(m, new PoisonPower(m, p, magicNumber));
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
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeBlock(1);
        upgradeMagicNumber(3);
        target = CardTarget.SELF_AND_ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = malchemy;
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = miasmodeus;
    }

    public void upgrade2() {
        upgradeBlock(1);
        upgradeMagicNumber(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        gifOverlay = vendemon;
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        gifOverlay = gumbaal;
    }

    @Override
    public boolean atBattleStartPreDraw() {
        if (info == 2) {
            addToBot(new GainBlockAction(Wiz.adp(), magicNumber));
        }
        return false;
    }
}