package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.BigExplosionVFX;
import RangerCaptain.vfx.BurnToAshEffect;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static RangerCaptain.MainModfile.makeID;

public class Bulletino extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Bulletino.class.getSimpleName());
    protected static Animation<TextureRegion> bulletino = loadGifOverlay("Bulletino_idle.gif");
    protected static Animation<TextureRegion> velocirifle = loadGifOverlay("Velocirifle_idle.gif");
    protected static Animation<TextureRegion> artillerex = loadGifOverlay("Artillerex_idle.gif");
    protected static Animation<TextureRegion> gearyu = loadGifOverlay("Gearyu_idle.gif");

    public Bulletino() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseMagicNumber = magicNumber = 2;
        gifOverlay = bulletino;
        baseInfo = info = 0;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            addToBot(new DrawCardAction(magicNumber));
        } else if (info == 1) {
            dmg(m, AbstractGameAction.AttackEffect.FIRE);
            addToBot(new DrawCardAction(magicNumber));
        } else if (info == 2) {
            addToBot(new ShakeScreenAction(0f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW));
            for (int i = 0 ; i < magicNumber ; i++) {
                addToBot(new DoAction(() -> Wiz.forAllMonstersLiving(mon -> {
                    AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(mon.hb.cX, mon.hb.cY));
                    AbstractDungeon.effectsQueue.add(new BurnToAshEffect(mon.hb.cX, mon.hb.cY));
                })));
                allDmg(AbstractGameAction.AttackEffect.NONE);
            }
            Wiz.forAllMonstersLiving(mon -> addToBot(new DrawCardAction(magicNumber)));
        } else if (info == 3) {
            addToBot(new DrawCardAction(magicNumber));
            addToBot(new DoAction(() -> {
                int hits = CardCounterPatches.cardsDrawnThisTurn.size() - CardCounterPatches.initialHand.size();
                for (int i = 0 ; i < hits ; i++) {
                    dmgTop(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                }
            }));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int currentDrawn = CardCounterPatches.cardsDrawnThisTurn.size() - CardCounterPatches.initialHand.size();
        int roomInHand = BaseMod.MAX_HAND_SIZE - Wiz.adp().hand.size();
        int cardsAvailable = Wiz.adp().drawPile.size() + Wiz.adp().discardPile.size();
        if (Wiz.adp().hasPower(NoDrawPower.POWER_ID)) {
            cardsAvailable = 0;
        }
        baseSecondMagic = secondMagic = currentDrawn + Math.min(magicNumber, Math.min(roomInHand, cardsAvailable));
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
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        setExclusions(1,2);
    }

    public void upgrade0() {
        forceUpgradeBaseCost(1);
        upgradeDamage(6);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = velocirifle;
        baseInfo = info = 1;
        exhaust = false;
    }

    public void upgrade1() {
        forceUpgradeBaseCost(2);
        upgradeMagicNumber(-1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = artillerex;
        baseInfo = info = 2;
    }

    public void upgrade2() {
        forceUpgradeBaseCost(3);
        upgradeDamage(-2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        gifOverlay = gearyu;
        baseInfo = info = 3;
    }
}