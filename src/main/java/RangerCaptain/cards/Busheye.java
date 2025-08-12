package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BurnComponent;
import RangerCaptain.cardfusion.components.EnergyComponent;
import RangerCaptain.cardfusion.components.WhenExhaustedComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.cards.interfaces.ManuallySizeAdjustedCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Busheye extends AbstractMultiUpgradeCard implements ManuallySizeAdjustedCard {
    public final static String ID = makeID(Busheye.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BUSHEYE)
                .withCost(2)
                .with(new BurnComponent(5, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new EnergyComponent(2))
                .withExhaust()
                .with(new WhenExhaustedComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.HUNTORCH)
                .withCost(2)
                .with(new BurnComponent(7, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new EnergyComponent(2))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.HEDGEHERNE)
                .withCost(2)
                .with(new BurnComponent(10, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new EnergyComponent(2))
                .withExhaust()
                .register();
    }

    public Busheye() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.BUSHEYE);
        exhaust = true;
        tags.add(CustomTags.MAGIC_BURN_AOE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void triggerOnExhaust() {
        addToTop(new GainEnergyAction(secondMagic));
        Wiz.forAllMonstersLiving(mon -> {
            Wiz.applyToEnemyTop(mon, new BurnedPower(mon, Wiz.adp(), magicNumber));
        });
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
    }

    public void upgrade0() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.HUNTORCH);
    }

    public void upgrade1() {
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.HEDGEHERNE);
    }

    @Override
    public float getAdjustedScale() {
        return 0.99f;
    }
}