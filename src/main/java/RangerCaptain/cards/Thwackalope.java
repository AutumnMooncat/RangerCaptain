package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.MakeCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Thwackalope extends AbstractEasyCard {
    public final static String ID = makeID(Thwackalope.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.THWACKALOPE)
                .withCost(2)
                .withMultiDamage(6, 2, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new MakeCardsComponent(1, new Wound(), true, MakeCardsComponent.Location.DRAW))
                .register();
    }

    public Thwackalope() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 9;
        setMonsterData(MonsterEnum.THWACKALOPE);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        cardsToPreview = new Wound();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new MakeTempCardInDrawPileAction(new Wound(), 1, true, true));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BRONZE, WHITE, BRONZE, WHITE, false);
    }
}