package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddReshuffleComponent;
import RangerCaptain.cardfusion.components.ConductiveComponent;
import RangerCaptain.cardfusion.components.MakeCopiesComponent;
import RangerCaptain.cardfusion.components.vfx.LightningOrbFVXComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import static RangerCaptain.MainModfile.makeID;

public class Boltam extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Boltam.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BOLTAM)
                .withCost(0)
                .withDamage(2.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningOrbFVXComponent())
                .with(new ConductiveComponent(2.5f), new MakeCopiesComponent(0.75f, MakeCopiesComponent.Location.DISCARD))
                .register();
        new FusionComponentHelper(MonsterEnum.PINBOLT)
                .withCost(0)
                .withDamage(4.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningOrbFVXComponent())
                .with(new ConductiveComponent(2.5f), new AddReshuffleComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.PLASMANTLER)
                .withCost(0)
                .withDamage(3.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningOrbFVXComponent())
                .with(new ConductiveComponent(3.5f), new MakeCopiesComponent(0.75f, MakeCopiesComponent.Location.DISCARD))
                .register();
    }

    public Boltam() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 3;
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.BOLTAM);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_CONDUCTIVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new LightningOrbActivateEffect(m.hb.cX, m.hb.cY)));
            addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
        }
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        Wiz.applyToEnemy(m, new ConductivePower(m, p, magicNumber));
        if (info == 0) {
            addToBot(new MakeTempCardInDiscardAction(makeStatEquivalentCopy(), secondMagic));
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
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.PINBOLT);
        baseInfo = info = 1;
        shuffleBackIntoDrawPile = true;
        uDesc();
    }

    public void upgrade1() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PLASMANTLER);
    }
}