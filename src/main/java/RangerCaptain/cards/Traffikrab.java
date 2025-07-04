package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionModHelper;
import RangerCaptain.cardmods.fusion.mods.NextTurnDrawMod;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import static RangerCaptain.MainModfile.makeID;

public class Traffikrab extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Traffikrab.class.getSimpleName());

    static {
        new FusionModHelper(MonsterEnum.TRAFFIKRAB)
                .withBlock(4)
                .with(new NextTurnDrawMod(1))
                .register();
        new FusionModHelper(MonsterEnum.WEEVILITE)
                .withBlock(6)
                .with(new NextTurnDrawMod(1))
                .register();
        new FusionModHelper(MonsterEnum.LOBSTACLE)
                .withBlock(6)
                .with(new NextTurnDrawMod(1))
                .register();
    }

    public Traffikrab() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 8;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.TRAFFIKRAB);
        tags.add(CustomTags.MAGIC_DRAW_NEXT_TURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToSelf(new DrawCardNextTurnPower(p, magicNumber));
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
        upgradeBaseCost(0);
        upgradeBlock(-2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WEEVILITE);
    }

    public void upgrade1() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.LOBSTACLE);
    }
}