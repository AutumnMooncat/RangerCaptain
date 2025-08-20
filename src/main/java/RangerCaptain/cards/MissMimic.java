package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.DieNextTurnComponent;
import RangerCaptain.cardfusion.components.GambitComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.GambitPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class MissMimic extends AbstractEasyCard {
    public final static String ID = makeID(MissMimic.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MISS_MIMIC)
                .withCost(1)
                .withFlags(new GambitComponent(3), AbstractComponent.Flag.REQUIRES_SAME_SOURCES)
                .withFlags(new DieNextTurnComponent(), AbstractComponent.Flag.REQUIRES_DIFFERENT_SOURCES)
                .withExhaust()
                .register();
    }

    public MissMimic() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.MISS_MIMIC);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new GambitPower(p, magicNumber));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }
}