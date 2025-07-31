package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.DrawComponent;
import RangerCaptain.cardmods.fusion.components.VulnerableComponent;
import RangerCaptain.cardmods.fusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Springheel extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Springheel.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SPRINGHEEL)
                .withCost(1)
                .withDamage(5, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new DrawComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.HOPSKIN)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new DrawComponent(2), new VulnerableComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.RIPTERRA)
                .withCost(1)
                .withDamage(7, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new DrawComponent(2), new VulnerableComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.SNOOPIN)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new DrawComponent(2), new WeakComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.SCAMPIRE)
                .withCost(1)
                .withDamage(7, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new DrawComponent(2), new WeakComponent(2))
                .register();
    }

    public Springheel() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 0;
        setMonsterData(MonsterEnum.SPRINGHEEL);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot(new DrawCardAction(magicNumber));
        if (info == 1) {
            Wiz.applyToEnemy(m, new VulnerablePower(m, secondMagic, false));
        } else if (info == 2) {
            Wiz.applyToEnemy(m, new WeakPower(m, secondMagic, false));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(IRIS), WHITE, darken(IRIS), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.HOPSKIN);
        baseInfo = info = 1;
        tags.add(CustomTags.MAGIC_VULN);
    }

    public void upgrade1() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.RIPTERRA);
    }

    public void upgrade2() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.SNOOPIN);
        baseInfo = info = 2;
        tags.add(CustomTags.MAGIC_WEAK);
    }

    public void upgrade3() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.SCAMPIRE);
    }
}