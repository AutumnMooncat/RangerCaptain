package RangerCaptain.cards;

import RangerCaptain.actions.CleansePowerAction;
import RangerCaptain.actions.DoublePowerAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddExhaustComponent;
import RangerCaptain.cardfusion.components.DoubleDebuffsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Magikrab extends AbstractEasyCard {
    public final static String ID = makeID(Magikrab.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MAGIKRAB)
                .withCost(1)
                .with(new DoubleDebuffsComponent(1), new AddExhaustComponent())
                .register();
    }

    public Magikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        setMonsterData(MonsterEnum.MAGIKRAB);
        setElementalType(ElementalType.ASTRAL);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DoublePowerAction(m, 1, CleansePowerAction.IS_DEBUFF));
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