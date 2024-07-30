package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static RangerCaptain.MainModfile.makeID;

public class Carniviper extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Carniviper.class.getSimpleName());
    protected static Animation<TextureRegion> carniviper = loadGifOverlay("Carniviper_idle.gif");
    protected static Animation<TextureRegion> masquerattle = loadGifOverlay("Masquerattle_idle.gif");
    protected static Animation<TextureRegion> aeroboros = loadGifOverlay("Aeroboros_idle.gif");
    protected static Animation<TextureRegion> maridusa = loadGifOverlay("Mardiusa_idle.gif");
    protected static Animation<TextureRegion> jormungold = loadGifOverlay("Jormungold_idle.gif");

    public Carniviper() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 3;
        gifOverlay = carniviper;
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        switch (info) {
            default:
                dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
                Wiz.applyToEnemy(m, new PoisonPower(m, p, magicNumber));
                break;
            case 1:
                dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                Wiz.applyToEnemy(m, new PoisonPower(m, p, magicNumber));
                Wiz.applyToEnemy(m, new PoisonPower(m, p, magicNumber));
                break;
        }
    }

    @Override
    public void triggerWhenDrawn() {
        if (info == 2) {
            Wiz.forAllMonstersLiving(mon -> {
                AbstractPower poison = mon.getPower(PoisonPower.POWER_ID);
                if (poison != null) {
                    addToBot(new LoseHPAction(mon, Wiz.adp(), poison.amount, AbstractGameAction.AttackEffect.POISON));
                }
            });
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        addUpgradeData(this::upgrade3, 0);
        setExclusions(1,2,3);
    }

    public void upgrade0() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = masquerattle;
    }

    public void upgrade1() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = aeroboros;
    }

    public void upgrade2() {
        upgradeBaseCost(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        gifOverlay = maridusa;
        baseInfo = info = 1;
    }

    public void upgrade3() {
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        gifOverlay = jormungold;
        baseInfo = info = 2;
    }
}