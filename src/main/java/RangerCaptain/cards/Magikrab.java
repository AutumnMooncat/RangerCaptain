package RangerCaptain.cards;

import RangerCaptain.actions.DoublePowerAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Magikrab extends AbstractEasyCard {
    public final static String ID = makeID(Magikrab.class.getSimpleName());
    protected static Animation<TextureRegion> magikrab = loadGifOverlay("Magikrab_idle.gif");

    public Magikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        gifOverlay = magikrab;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DoublePowerAction(m, 1, pow -> pow.type == AbstractPower.PowerType.DEBUFF));
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