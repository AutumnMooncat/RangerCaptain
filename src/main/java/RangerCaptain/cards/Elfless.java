package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.MonsterAttackedPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Elfless extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Elfless.class.getSimpleName());
    protected static Animation<TextureRegion> elfless = loadGifOverlay("Elfless_idle.gif");
    protected static Animation<TextureRegion> faerious = loadGifOverlay("Faerious_idle.gif");
    protected static Animation<TextureRegion> grampus = loadGifOverlay("Grampus_idle.gif");

    public Elfless() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 12;
        baseMagicNumber = magicNumber = 2;
        gifOverlay = elfless;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, info == 1 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new DoAction(() -> {
            if (m != null && m.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                addToTop(new GainEnergyAction(magicNumber));
            }
        }));
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

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (info == 2 && mo.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
            damage *= 2;
            isDamageModified = true;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
        Wiz.forAllMonstersLiving(mon -> {
            if (mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR;
            }
        });
    }

    public void upgrade0() {
        upgradeDamage(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = faerious;
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBaseCost(3);
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = grampus;
        baseInfo = info = 2;
    }
}