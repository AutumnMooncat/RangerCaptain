package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BurnComponent;
import RangerCaptain.cardfusion.components.DamageComponent;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.vfx.ExplodeAllVFXComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
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
                .with(new DrawComponent(2))
                .withFlags(new BurnComponent(2), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.VELOCIRIFLE)
                .withCost(1)
                .with(new DrawComponent(3))
                .withFlags(new BurnComponent(2), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .register();
        new FusionComponentHelper(MonsterEnum.ARTILLEREX)
                .withCost(2)
                .with(new DrawComponent(4))
                .withFlags(new ExplodeAllVFXComponent(), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .withFlags(new BurnComponent(2, AbstractComponent.ComponentTarget.ENEMY_AOE), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .register();
        new FusionComponentHelper(MonsterEnum.GEARYU)
                .withCost(3)
                .withFlags(new DamageComponent(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY), AbstractComponent.Flag.DRAW_FOLLOWUP)
                .with(new DrawComponent(5))
                .register();
    }

    public Bulletino() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.BULLETINO);
        baseInfo = info = 0;
        tags.add(CustomTags.MAGIC_DRAW);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0 || info == 1) {
            addToBot(new DrawCardAction(magicNumber, new DoAction(() -> {
                int hits = DrawCardAction.drawnCards.size();
                for (int i = 0 ; i < hits ; i++) {
                    Wiz.applyToEnemyTop(m, new BurnedPower(m, p, secondMagic));
                }
            })));
        } else if (info == 2) {
            addToBot(new DrawCardAction(magicNumber, new DoAction(() -> {
                int hits = DrawCardAction.drawnCards.size();
                for (int i = 0 ; i < hits ; i++) {
                    for (int j = AbstractDungeon.getMonsters().monsters.size() - 1; j >= 0; j--) {
                        AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(j);
                        if (!mon.isDeadOrEscaped()) {
                            Wiz.applyToEnemyTop(mon, new BurnedPower(mon, p, secondMagic));
                        }
                    }
                    addToTop(new ShakeScreenAction(0f, ScreenShake.ShakeDur.SHORT, ScreenShake.ShakeIntensity.LOW));
                    addToTop(new DoAction(() -> Wiz.forAllMonstersLiving(mon -> {
                        AbstractDungeon.effectsQueue.add(new ExplosionSmallEffect(mon.hb.cX, mon.hb.cY));
                        AbstractDungeon.effectsQueue.add(new BurnToAshEffect(mon.hb.cX, mon.hb.cY));
                    })));
                }
            })));
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
        target = CardTarget.ALL_ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.ARTILLEREX);
        baseInfo = info = 2;
    }

    public void upgrade2() {
        forceUpgradeBaseCost(3);
        if (baseDamage == -1) {
            baseDamage = 0;
        }
        upgradeDamage(8);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterEnum.GEARYU);
        baseInfo = info = 3;
        type = CardType.ATTACK;
        rollerKey += "Attack";
        CardArtRoller.computeCard(this);
    }
}