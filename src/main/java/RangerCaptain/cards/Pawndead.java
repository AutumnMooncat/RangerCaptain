package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.SummonedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.unique.NightmareAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Pawndead extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Pawndead.class.getSimpleName());
    protected static Animation<TextureRegion> pawndead = loadGifOverlay("Pawndead_idle.gif");
    protected static Animation<TextureRegion> skelevangelist = loadGifOverlay("Skelevangelist_idle.gif");
    protected static Animation<TextureRegion> kingrave = loadGifOverlay("Kingrave_idle.gif");
    protected static Animation<TextureRegion> queenyx = loadGifOverlay("Queenyx_idle.gif");

    public Pawndead() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        gifOverlay = pawndead;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(1, NightmareAction.TEXT[0], false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.applyToSelfTop(new SummonedPower(p, magicNumber, card));
            }
        }));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(darken(BLUE)), WHITE, darken(darken(BLUE)), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        setExclusions(1,2);
    }

    public void upgrade0() {
        upgradeBaseCost(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = skelevangelist;
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        gifOverlay = kingrave;
    }

    public void upgrade2() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        gifOverlay = queenyx;
    }
}