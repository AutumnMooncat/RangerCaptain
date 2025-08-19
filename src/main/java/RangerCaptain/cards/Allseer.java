package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.ConductiveComponent;
import RangerCaptain.cardfusion.components.vfx.LaserVFXComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static RangerCaptain.MainModfile.makeID;

public class Allseer extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Allseer.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.ALLSEER)
                .withCost(1)
                .withBlock(4.5f)
                .with(new LaserVFXComponent())
                .with(new ConductiveComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.KHUFO)
                .withCost(1)
                .withBlock(6.5f)
                .with(new LaserVFXComponent())
                .with(new ConductiveComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.TRIPHINX)
                .withCost(1)
                .withBlock(4.5f)
                .with(new LaserVFXComponent())
                .with(new ConductiveComponent(4))
                .register();
    }

    public Allseer() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.ALLSEER);
        tags.add(CustomTags.MAGIC_CONDUCTIVE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (m != null) {
            addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
            addToBot(new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
        }
        addToBot(new ApplyPowerAction(m, p, new ConductivePower(m, p, magicNumber)));
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, Color.DARK_GRAY, WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.KHUFO);
    }

    public void upgrade1() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.TRIPHINX);
    }
}