package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.MakeCardsComponent;
import RangerCaptain.cardmods.fusion.components.StrengthComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static RangerCaptain.MainModfile.makeID;

public class DjinnEntonic extends AbstractEasyCard {
    public final static String ID = makeID(DjinnEntonic.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DJINN_ENTONIC)
                .withCost(0)
                .with(new StrengthComponent(3))
                .with(new MakeCardsComponent(2, new Dazed(), MakeCardsComponent.Location.DRAW))
                .register();
    }

    public DjinnEntonic() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.DJINN_ENTONIC);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        cardsToPreview = new Dazed();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new StrengthPower(p, magicNumber));
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), magicNumber, true, true));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(PURPLE), WHITE, darken(PURPLE), WHITE, false);
    }
}