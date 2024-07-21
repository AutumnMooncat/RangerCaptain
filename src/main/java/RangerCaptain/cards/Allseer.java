package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.CloseEncounterPower;
import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static RangerCaptain.MainModfile.makeID;

public class Allseer extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Allseer.class.getSimpleName());
    protected static Animation<TextureRegion> allseer = loadGifOverlay("Allseer_idle.gif");
    protected static Animation<TextureRegion> khufo = loadGifOverlay("Khufo_idle.gif");
    protected static Animation<TextureRegion> triphinx = loadGifOverlay("Triphinx_idle.gif");

    public Allseer() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 1;
        gifOverlay = allseer;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (m != null && info == 0) {
            addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
            addToBot(new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
        }
        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false), magicNumber, info == 0 ? AbstractGameAction.AttackEffect.NONE : AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (info == 1) {
            Allseer copy = (Allseer) this.makeSameInstanceOf();
            copy.info = copy.baseInfo = -1;
            Wiz.applyToSelf(new MindMeldPower(p, copy));
        } else if (info == 2) {
            Wiz.applyToSelf(new CloseEncounterPower(p, this));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, Color.DARK_GRAY, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBlock(-3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = khufo;
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = triphinx;
        info = baseInfo = 2;
    }
}