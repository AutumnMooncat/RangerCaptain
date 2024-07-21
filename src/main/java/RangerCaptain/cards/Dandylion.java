package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.LeechedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Dandylion extends AbstractEasyCard {
    public final static String ID = makeID(Dandylion.class.getSimpleName());
    protected static Animation<TextureRegion> dandylion = loadGifOverlay("Dandylion_idle.gif");
    protected static Animation<TextureRegion> blossomaw = loadGifOverlay("Blossomaw_idle.gif");

    public Dandylion() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 5;
        baseMagicNumber = magicNumber = 3;
        gifOverlay = dandylion;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToEnemy(m, new LeechedPower(m, p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = blossomaw;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }
}