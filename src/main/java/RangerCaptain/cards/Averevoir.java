package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.TowerDefencePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Averevoir extends AbstractEasyCard {
    public final static String ID = makeID(Averevoir.class.getSimpleName());
    protected static Animation<TextureRegion> averevoir = loadGifOverlay("Averevoir_stance_idle.gif");

    public Averevoir() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        gifOverlay = averevoir;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new TowerDefencePower(p, 1));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }
}