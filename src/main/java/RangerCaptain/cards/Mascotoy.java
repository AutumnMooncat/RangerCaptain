package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Mascotoy extends AbstractEasyCard {
    public final static String ID = makeID(Mascotoy.class.getSimpleName());
    protected static Animation<TextureRegion> mascotoy = loadGifOverlay("Mascotoy_idle.gif");
    protected static Animation<TextureRegion> mascotorn = loadGifOverlay("Mascotorn_idle.gif");

    public Mascotoy() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseMagicNumber = magicNumber = 1;
        baseInfo = info = 1;
        gifOverlay = mascotoy;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (info == 1) {
            Mascotoy copy = (Mascotoy) makeSameInstanceOf();
            copy.baseInfo = copy.info = 0;
            Wiz.applyToSelf(new MindMeldPower(p, copy));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = mascotorn;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, Color.PINK, WHITE, Color.PINK, WHITE, false);
    }
}