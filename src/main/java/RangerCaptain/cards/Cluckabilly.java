package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static RangerCaptain.MainModfile.makeID;

public class Cluckabilly extends AbstractEasyCard {
    public final static String ID = makeID(Cluckabilly.class.getSimpleName());
    protected static Animation<TextureRegion> clickabilly = loadGifOverlay("Cluckabilly_idle.gif");
    protected static Animation<TextureRegion> rockertrice = loadGifOverlay("Rockertrice_idle.gif");

    public Cluckabilly() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        baseMagicNumber = magicNumber = 1;
        gifOverlay = clickabilly;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new VulnerablePower(mon, magicNumber, false)));
        Wiz.applyToSelf(new MultitargetPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = rockertrice;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BRONZE), WHITE, darken(BRONZE), WHITE, false);
    }
}