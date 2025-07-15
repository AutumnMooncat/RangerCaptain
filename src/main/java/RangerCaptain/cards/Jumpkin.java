package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.ToxinComponent;
import RangerCaptain.cardmods.fusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Jumpkin extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Jumpkin.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.JUMPKIN)
                .withCost(1)
                .with(new ToxinComponent(3))
                .with(new WeakComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.DRACULEAF)
                .withCost(1)
                .with(new ToxinComponent(3, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .with(new WeakComponent(1, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.BEANSTALKER)
                .withCost(1)
                .with(new ToxinComponent(4))
                .with(new WeakComponent(2))
                .register();
    }

    public Jumpkin() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.JUMPKIN);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_TOXIN);
        tags.add(CustomTags.SECOND_MAGIC_WEAK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            Wiz.applyToEnemy(m, new ToxinPower(m, magicNumber));
            Wiz.applyToEnemy(m, new WeakPower(m, secondMagic, false));
        } else {
            Wiz.forAllMonstersLiving(mon -> {
                Wiz.applyToEnemy(mon, new ToxinPower(mon, magicNumber));
                Wiz.applyToEnemy(mon, new WeakPower(mon, secondMagic, false));
            });
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, ORANGE, WHITE, ORANGE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        baseInfo = info = 1;
        target = CardTarget.ALL_ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.DRACULEAF);
        tags.add(CustomTags.MAGIC_TOXIN_AOE);
        tags.remove(CustomTags.MAGIC_TOXIN);
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.BEANSTALKER);
    }
}