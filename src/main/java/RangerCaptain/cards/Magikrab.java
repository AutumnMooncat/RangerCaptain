package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static RangerCaptain.MainModfile.makeID;

public class Magikrab extends AbstractEasyCard {
    public final static String ID = makeID(Magikrab.class.getSimpleName());
    protected static Animation<TextureRegion> magikrab = loadGifOverlay("Magikrab_idle.gif");

    public Magikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        gifOverlay = magikrab;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new IntangiblePlayerPower(p, 1));
        Wiz.applyToSelf(new VulnerablePower(p, 2, false));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }
}