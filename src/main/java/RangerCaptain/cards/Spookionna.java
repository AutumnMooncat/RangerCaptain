package RangerCaptain.cards;

import RangerCaptain.actions.ApplyPowerActionWithFollowup;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static RangerCaptain.MainModfile.makeID;

public class Spookionna extends AbstractEasyCard {
    public final static String ID = makeID(Spookionna.class.getSimpleName());
    protected static Animation<TextureRegion> spookionna = loadGifOverlay("Spooki-onna_idle.gif");

    public Spookionna() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);
        baseMagicNumber = magicNumber = 8;
        gifOverlay = spookionna;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerActionWithFollowup(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)), new ApplyPowerAction(p, p, new GainStrengthPower(p, magicNumber))));
        Wiz.forAllMonstersLiving(mon -> addToBot(new ApplyPowerActionWithFollowup(new ApplyPowerAction(mon, p, new StrengthPower(mon, -magicNumber)), new ApplyPowerAction(mon, p, new GainStrengthPower(mon, magicNumber)))));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (!AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
                canUse = false;
                cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            }
            return canUse;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}