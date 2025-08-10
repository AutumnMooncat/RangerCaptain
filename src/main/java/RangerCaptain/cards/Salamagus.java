package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.AddMindMeldComponent;
import RangerCaptain.cardmods.fusion.components.BurnComponent;
import RangerCaptain.cardmods.fusion.components.OnExhaustComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.powers.FlammablePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Salamagus extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Salamagus.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SALAMAGUS)
                .withCost(1)
                .with(new OnExhaustComponent())
                .withFlags(new BurnComponent(8), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.ADEPTILE)
                .withCost(2)
                .with(new OnExhaustComponent())
                .withFlags(new BurnComponent(19), AbstractComponent.Flag.INVERSE_PREFERRED)
                .with(new AddMindMeldComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.PYROMELEON)
                .withCost(1)
                .with(new OnExhaustComponent())
                .withFlags(new BurnComponent(13), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Salamagus() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.SALAMAGUS);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new FlammablePower(p, magicNumber));
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
        upgradeBaseCost(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.ADEPTILE);
        baseInfo = info = 1;
        tags.add(CustomTags.MIND_MELD);
        ExtraEffectPatches.EffectFields.mindMeld.set(this, true);
    }

    public void upgrade1() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PYROMELEON);
    }
}