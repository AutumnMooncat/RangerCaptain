package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.ConductiveComponent;
import RangerCaptain.cardmods.fusion.components.OnGainEnergyComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.EnergyReservesPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Amphare extends AbstractEasyCard {
    public final static String ID = makeID(Amphare.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.AMPHARE)
                .withCost(1)
                .with(new OnGainEnergyComponent())
                .withFlags(new ConductiveComponent(4, AbstractComponent.ComponentTarget.ENEMY_AOE), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.LAPACITOR)
                .withCost(1)
                .with(new OnGainEnergyComponent())
                .withFlags(new ConductiveComponent(6, AbstractComponent.ComponentTarget.ENEMY_AOE), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Amphare() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.AMPHARE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new EnergyReservesPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.LAPACITOR);
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