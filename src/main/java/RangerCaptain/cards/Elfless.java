package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.FreeWhenPlayedComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Elfless extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Elfless.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.ELFLESS)
                .withCost(2)
                .withDamage(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new FreeWhenPlayedComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.FAERIOUS)
                .withCost(2)
                .withDamage(10, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new FreeWhenPlayedComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.GRAMPUS)
                .withCost(3)
                .withDamage(12, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new FreeWhenPlayedComponent())
                .register();
    }

    public Elfless() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 10;
        setMonsterData(MonsterEnum.ELFLESS);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, info == 1 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new DoAction(() -> {
            cost = costForTurn = 0;
            isCostModified = true;
        }));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FAERIOUS);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBaseCost(3);
        upgradeDamage(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.GRAMPUS);
        baseInfo = info = 2;
    }
}