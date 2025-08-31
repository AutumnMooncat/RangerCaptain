package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.BadForecastComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.AttackEffectPatches;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.BadForecastPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Umbrahella extends AbstractEasyCard {
    public final static String ID = makeID(Umbrahella.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.UMBRAHELLA)
                .withCost(2)
                .withDamage(11, AttackEffectPatches.RANGER_CAPTAIN_TOXIN)
                .with(new BadForecastComponent(1.91f))
                .register();
    }

    public Umbrahella() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setMonsterData(MonsterEnum.UMBRAHELLA);
        baseDamage = damage = 15;
        baseMagicNumber = magicNumber = 2;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffectPatches.RANGER_CAPTAIN_TOXIN);
        Wiz.applyToEnemy(m, new BadForecastPower(m, p, magicNumber));
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