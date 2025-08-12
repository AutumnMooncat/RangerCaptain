package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.MakeCardsComponent;
import RangerCaptain.cardfusion.components.OnTurnStartComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.tokens.Sludge;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.AcidReflexPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Jellyton extends AbstractEasyCard {
    public final static String ID = makeID(Jellyton.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.JELLYTON)
                .withCost(1)
                .with(new OnTurnStartComponent())
                .withFlags(new MakeCardsComponent(2, new Sludge(), false, MakeCardsComponent.Location.HAND), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Jellyton() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.JELLYTON);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        cardsToPreview = new Sludge();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new AcidReflexPower(p, magicNumber));
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