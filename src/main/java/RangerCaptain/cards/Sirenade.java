package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Sirenade extends AbstractEasyCard {
    public final static String ID = makeID(Sirenade.class.getSimpleName());
    protected static Animation<TextureRegion> sirenade = loadGifOverlay("Sirenade_idle.gif");
    protected static Animation<TextureRegion> decibelle = loadGifOverlay("Decibelle_idle.gif");

    public Sirenade() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 1;
        isMultiDamage = true;
        gifOverlay = sirenade;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = decibelle;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, PURPLE, WHITE, PURPLE, WHITE, false);
    }
}