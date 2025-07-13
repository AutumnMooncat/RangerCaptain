package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.AddExhaustComponent;
import RangerCaptain.cardmods.fusion.components.BurnComponent;
import RangerCaptain.cardmods.fusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Busheye extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Busheye.class.getSimpleName());

    static {
        // 7 -> 14
        new FusionComponentHelper(MonsterEnum.BUSHEYE)
                .withCost(2)
                .with(new BurnComponent(4, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new WeakComponent(1, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new AddExhaustComponent())
                .register();
        // 10 -> 21
        new FusionComponentHelper(MonsterEnum.HUNTORCH)
                .withCost(2)
                .with(new BurnComponent(6, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new WeakComponent(1, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new AddExhaustComponent())
                .register();
        // 14 -> 28
        new FusionComponentHelper(MonsterEnum.HEDGEHERNE)
                .withCost(2)
                .with(new BurnComponent(8, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new WeakComponent(1, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new AddExhaustComponent())
                .register();
    }

    public Busheye() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 7;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.BUSHEYE);
        exhaust = true;
        tags.add(CustomTags.SECOND_MAGIC_WEAK_AOE);
        tags.add(CustomTags.MAGIC_BURN_AOE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mon -> {
            Wiz.applyToEnemy(mon, new BurnedPower(mon, p, magicNumber));
            Wiz.applyToEnemy(mon, new WeakPower(mon, secondMagic, false));
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
        upgradeMagicNumber(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.HUNTORCH);
    }

    public void upgrade1() {
        upgradeMagicNumber(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.HEDGEHERNE);
    }
}