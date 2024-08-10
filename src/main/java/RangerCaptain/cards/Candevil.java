package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static RangerCaptain.MainModfile.makeID;

public class Candevil extends AbstractMultiUpgradeCard implements StartupCard {
    public final static String ID = makeID(Candevil.class.getSimpleName());
    public Candevil() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 9;
        baseMagicNumber = magicNumber = 0;
        setMonsterData(MonsterEnum.CANDEVIL);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (info == 1) {
            Wiz.applyToEnemy(m, new PoisonPower(m, p, magicNumber));
        }
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
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeBlock(1);
        upgradeMagicNumber(3);
        target = CardTarget.SELF_AND_ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MALCHEMY);
        baseInfo = info = 1;
        tags.add(CustomTags.MAGIC_POISON);
    }

    public void upgrade1() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MIASMODEUS);
    }

    public void upgrade2() {
        upgradeBlock(1);
        upgradeMagicNumber(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.VENDEMON);
        baseInfo = info = 2;
    }

    public void upgrade3() {
        upgradeBlock(2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterEnum.GUMBAAL);
    }

    @Override
    public boolean atBattleStartPreDraw() {
        if (info == 2) {
            addToBot(new GainBlockAction(Wiz.adp(), magicNumber));
        }
        return false;
    }
}