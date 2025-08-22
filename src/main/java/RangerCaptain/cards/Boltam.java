package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddReshuffleComponent;
import RangerCaptain.cardfusion.components.ConductiveComponent;
import RangerCaptain.cardfusion.components.vfx.LightningOrbFVXComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
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
                .with(new ConductiveComponent(2.5f), new AddReshuffleComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.PINBOLT)
                .withCost(0)
                .withMultiDamage(0.5f, 3, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningOrbFVXComponent())
                .with(new ConductiveComponent(2.5f), new AddReshuffleComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.PLASMANTLER)
                .withCost(0)
                .withDamage(4, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningOrbFVXComponent())
                .with(new ConductiveComponent(2.5f), new AddReshuffleComponent())
                .register();
    }

    public Boltam() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 3;
        baseMagicNumber = magicNumber = 3;
        baseInfo = info = 0;
        setMonsterData(MonsterEnum.BOLTAM);
        tags.add(CustomTags.MAGIC_CONDUCTIVE);
        shuffleBackIntoDrawPile = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hits = info == 0 ? 1 : 3;
        for (int i = 0; i < hits; i++) {
            if (m != null) {
                addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
                addToBot(new VFXAction(new LightningOrbActivateEffect(m.hb.cX, m.hb.cY)));
            }
            dmg(m, AbstractGameAction.AttackEffect.NONE);
        }
        Wiz.applyToEnemy(m, new ConductivePower(m, p, magicNumber));
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
        upgradeDamage(-2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.PINBOLT);
        baseInfo = info = 1;
        uDesc();
    }

    public void upgrade1() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.PLASMANTLER);
    }
}