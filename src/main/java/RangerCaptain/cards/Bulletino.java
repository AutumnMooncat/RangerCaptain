package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.DamageComponent;
import RangerCaptain.cardmods.fusion.components.DrawComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.BurnToAshEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static RangerCaptain.MainModfile.makeID;

public class Bulletino extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Bulletino.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BULLETINO)
                .withCost(0)
                .withDamage(10, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new DrawComponent(2))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.VELOCIRIFLE)
                .withCost(1)
                .withDamage(8, AbstractGameAction.AttackEffect.FIRE)
                .with(new DrawComponent(3))
                .register();
        new FusionComponentHelper(MonsterEnum.ARTILLEREX)
                .withCost(2)
                .withMultiDamageAOE(4, 2, AbstractGameAction.AttackEffect.NONE)
                .with(new DrawComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.GEARYU)
                .withCost(3)
                .withFlags(new DamageComponent(2, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .with(new DrawComponent(2))
                .register();
    }

    public Bulletino() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.BULLETINO);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_DRAW);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            addToBot(new DrawCardAction(magicNumber));
        } else if (info == 1) {
            dmg(m, AbstractGameAction.AttackEffect.FIRE);
            addToBot(new DrawCardAction(magicNumber));
        } else if (info == 2) {
            addToBot(new ShakeScreenAction(0f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW));
            for (int i = 0 ; i < thirdMagic ; i++) {
                addToBot(new DoAction(() -> Wiz.forAllMonstersLiving(mon -> {
                    AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(mon.hb.cX, mon.hb.cY));
                    AbstractDungeon.effectsQueue.add(new BurnToAshEffect(mon.hb.cX, mon.hb.cY));
                })));
                allDmg(AbstractGameAction.AttackEffect.NONE);
            }
            addToBot(new DrawCardAction(magicNumber));
        } else if (info == 3) {
            addToBot(new DrawCardAction(magicNumber, new DoAction(() -> {
                int hits = DrawCardAction.drawnCards.size();
                for (int i = 0 ; i < hits ; i++) {
                    dmgTop(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                }
            })));
        }
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
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2, 0);
        setExclusions(1,2);
    }

    public void upgrade0() {
        forceUpgradeBaseCost(1);
        upgradeDamage(6);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.VELOCIRIFLE);
        baseInfo = info = 1;
        exhaust = false;
    }

    public void upgrade1() {
        forceUpgradeBaseCost(2);
        upgradeMagicNumber(1);
        baseThirdMagic = 0;
        upgradeThirdMagic(2);
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
        tags.add(CustomTags.AOE_DAMAGE);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.ARTILLEREX);
        baseInfo = info = 2;
    }

    public void upgrade2() {
        forceUpgradeBaseCost(3);
        upgradeDamage(-2);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.GEARYU);
        baseInfo = info = 3;
    }
}