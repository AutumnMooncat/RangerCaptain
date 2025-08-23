package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.SharpenComponent;
import RangerCaptain.cardfusion.components.VigorComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.SharpenPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Squirey extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Squirey.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SQUIREY)
                .withCost(1)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new VigorComponent(1.91f), new SharpenComponent(0.75f))
                .register();
        new FusionComponentHelper(MonsterEnum.MANISPEAR)
                .withCost(2)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new VigorComponent(1.91f), new SharpenComponent(1.91f))
                .register();
        new FusionComponentHelper(MonsterEnum.PALANGOLIN)
                .withCost(1)
                .withDamage(7, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new VigorComponent(2.91f), new SharpenComponent(0.75f))
                .register();
    }

    public Squirey() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.SQUIREY);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        } else if (info == 1) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        } else if (info == 2) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
        Wiz.applyToSelf(new VigorPower(p, magicNumber));
        Wiz.applyToSelf(new SharpenPower(p, secondMagic));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BRONZE, WHITE, BRONZE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBaseCost(2);
        upgradeDamage(4);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MANISPEAR);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PALANGOLIN);
        baseInfo = info = 2;
    }
}