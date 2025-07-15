package RangerCaptain.cards;

import RangerCaptain.actions.CleansePowerAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.RemoveBuffForBlockComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Padpole extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Padpole.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.PADPOLE)
                .withCost(1)
                .withBlock(5)
                .with(new RemoveBuffForBlockComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.FRILLYPAD)
                .withCost(1)
                .withBlock(7)
                .with(new RemoveBuffForBlockComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.LILIGATOR)
                .withCost(1)
                .withBlock(9)
                .with(new RemoveBuffForBlockComponent())
                .register();
    }

    public Padpole() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 7;
        setMonsterData(MonsterEnum.PADPOLE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new CleansePowerAction(p, 1, true, power -> power.type == AbstractPower.PowerType.BUFF, removed -> {
            if (!removed.isEmpty()) {
                blckTop();
            }
        }));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
    }

    public void upgrade0() {
        upgradeBlock(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FRILLYPAD);
    }

    public void upgrade1() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.LILIGATOR);
    }
}