package RangerCaptain.cards;

import RangerCaptain.actions.EasyXCostAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

import static RangerCaptain.MainModfile.makeID;

public class Umbrahella extends AbstractEasyCard {
    public final static String ID = makeID(Umbrahella.class.getSimpleName());
    protected static Animation<TextureRegion> umbrahella = loadGifOverlay("Umbrahella_idle.gif");

    public Umbrahella() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        gifOverlay = umbrahella;
        baseDamage = damage = 7;
        isMultiDamage = true;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EasyXCostAction(this, (base, params) -> {
            int effect = base + Arrays.stream(params).sum();
            if (effect > 0) {
                for (int i = 0 ; i < multiDamage.length ; i++) {
                    multiDamage[i] *= effect;
                }
                allDmg(AbstractGameAction.AttackEffect.POISON);
            }
            return true;
        }));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}