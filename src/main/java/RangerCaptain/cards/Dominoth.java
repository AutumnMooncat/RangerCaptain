package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionModHelper;
import RangerCaptain.cardmods.fusion.mods.ConductiveMod;
import RangerCaptain.cardmods.fusion.mods.VigorMod;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static RangerCaptain.MainModfile.makeID;

public class Dominoth extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Dominoth.class.getSimpleName());

    static {
        new FusionModHelper(MonsterEnum.DOMINOTH)
                .with(new VigorMod(4))
                .register();
        new FusionModHelper(MonsterEnum.WINGLOOM)
                .with(new ConductiveMod(4))
                .register();
        new FusionModHelper(MonsterEnum.MOTHMANIC)
                .with(new ConductiveMod(6))
                .register();
        new FusionModHelper(MonsterEnum.TOKUSECT)
                .with(new VigorMod(7))
                .register();
    }

    public Dominoth() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 0;
        setMonsterData(MonsterEnum.DOMINOTH);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_DRAW);
        tags.add(CustomTags.SECOND_MAGIC_VIGOR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            Wiz.applyToEnemy(m, new ConductivePower(m, p, (int) (secondMagic + Wiz.adp().hand.group.stream().filter(c -> c != this).count())));
        }
        if (info == 0) {
            Wiz.applyToSelf(new VigorPower(p, (int) (secondMagic + Wiz.adp().hand.group.stream().filter(c -> c != this).count())));
        }
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(darken(BLUE)), WHITE, darken(darken(BLUE)), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WINGLOOM);
        baseInfo = info = 1;
        target = CardTarget.ENEMY;
        tags.add(CustomTags.SECOND_MAGIC_CONDUCTIVE);
        tags.remove(CustomTags.SECOND_MAGIC_VIGOR);
    }

    public void upgrade1() {
        //upgradeMagicNumber(1);
        upgradeSecondMagic(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MOTHMANIC);
    }

    public void upgrade2() {
        upgradeSecondMagic(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.TOKUSECT);
    }
}