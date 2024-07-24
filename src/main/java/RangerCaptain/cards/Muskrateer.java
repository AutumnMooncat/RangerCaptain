package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.defect.FTLAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Muskrateer extends AbstractEasyCard {
    public final static String ID = makeID(Muskrateer.class.getSimpleName());
    protected static Animation<TextureRegion> muskrateer = loadGifOverlay("Muskrateer_idle.gif");
    protected static Animation<TextureRegion> ratcousel = loadGifOverlay("Ratcousel_idle.gif");

    public Muskrateer() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 3;
        baseMagicNumber = magicNumber = 2;
        gifOverlay = muskrateer;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new FTLAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.magicNumber));
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        rawDescription = cardStrings.DESCRIPTION;
        rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[1] + count;
        if (count == 1) {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[2];
        } else {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[3];
        }
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractDungeon.actionManager.cardsPlayedThisTurn.size() < magicNumber ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = ratcousel;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}