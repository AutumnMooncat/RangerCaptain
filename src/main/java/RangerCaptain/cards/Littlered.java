package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnTurnStartForEachAttackerComponent;
import RangerCaptain.cardmods.fusion.components.StrengthComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.powers.BerserkerPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Littlered extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Littlered.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.LITTLERED)
                .withCost(2)
                .with(new OnTurnStartForEachAttackerComponent())
                .withFlags(new StrengthComponent(1), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.SCARLETEETH)
                .withCost(2)
                .with(new OnTurnStartForEachAttackerComponent())
                .withFlags(new StrengthComponent(2), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.ROSEHOOD)
                .withCost(1)
                .with(new OnTurnStartForEachAttackerComponent())
                .withFlags(new StrengthComponent(1), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Littlered() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.LITTLERED);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new BerserkerPower(p, magicNumber));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(RED), WHITE, darken(RED), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SCARLETEETH);
        baseInfo = info = 1;
    }

    public void upgrade1() {
        upgradeBaseCost(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.ROSEHOOD);
        info = baseInfo = 2;
        isInnate = true;
    }
}