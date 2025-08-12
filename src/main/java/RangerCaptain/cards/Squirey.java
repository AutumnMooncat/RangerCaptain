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
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new VigorComponent(2), new SharpenComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.MANISPEAR)
                .withCost(1)
                .withBlock(4)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new VigorComponent(3), new SharpenComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.PALANGOLIN)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new VigorComponent(2), new SharpenComponent(2))
                .register();
    }

    public Squirey() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
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
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MANISPEAR);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PALANGOLIN);
        baseInfo = info = 2;
    }
}