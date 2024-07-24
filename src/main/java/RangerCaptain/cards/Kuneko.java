package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;

import static RangerCaptain.MainModfile.makeID;

public class Kuneko extends AbstractEasyCard {
    public final static String ID = makeID(Kuneko.class.getSimpleName());
    protected static Animation<TextureRegion> kuneko = loadGifOverlay("Kuneko_idle.gif");
    protected static Animation<TextureRegion> shiningKuneko = loadGifOverlay("Shining_Kuneko_idle.gif");

    public Kuneko() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = damage = 20;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
        gifOverlay = kuneko;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int[] alive = {0};
        addToBot(new DoAction(() -> alive[0] = Wiz.getEnemies().size()));
        addToBot(new VFXAction(new BorderLongFlashEffect(Color.LIGHT_GRAY)));
        addToBot(new VFXAction(new DieDieDieEffect(), 0.7F));
        addToBot(new ShakeScreenAction(0.0F, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
        allDmg(AbstractGameAction.AttackEffect.SLASH_HEAVY);
        addToBot(new DoAction(() -> {
            for (int i = 0; i < alive[0] - Wiz.getEnemies().size(); i++) {
                addToTop(new GainEnergyAction(magicNumber));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(8);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = shiningKuneko;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(PURPLE), WHITE, darken(PURPLE), WHITE, false);
    }
}