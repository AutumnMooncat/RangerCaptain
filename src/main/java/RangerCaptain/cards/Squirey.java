package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionModHelper;
import RangerCaptain.cardmods.fusion.mods.TempStrengthMod;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static RangerCaptain.MainModfile.makeID;

public class Squirey extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Squirey.class.getSimpleName());

    static {
        new FusionModHelper(MonsterEnum.SQUIREY)
                .withDamage(4, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new TempStrengthMod(2))
                .register();
        new FusionModHelper(MonsterEnum.MANISPEAR)
                .withBlock(3)
                .withDamage(3, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new TempStrengthMod(5))
                .register();
        new FusionModHelper(MonsterEnum.PALANGOLIN)
                .withDamage(5, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new TempStrengthMod(4))
                .register();
    }

    public Squirey() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.SQUIREY);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            blck();
        }
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        Wiz.applyToSelf(new StrengthPower(p, magicNumber));
        Wiz.applyToSelf(new LoseStrengthPower(p, magicNumber));
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
        if (baseBlock < 0) {
            baseBlock = 0;
        }
        upgradeBlock(6);
        upgradeDamage(-2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MANISPEAR);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PALANGOLIN);
    }
}