package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.DamageLastAttackerComponent;
import RangerCaptain.cardmods.fusion.components.TapeJamComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Diveal extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Diveal.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.DIVEAL)
                .withCost(1)
                .with(new DamageLastAttackerComponent(9, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .register();
        new FusionComponentHelper(MonsterEnum.DIVEBERG)
                .withCost(1)
                .with(new DamageLastAttackerComponent(9, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new TapeJamComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.SCUBALRUS)
                .withCost(1)
                .with(new DamageLastAttackerComponent(3, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .register();
    }

    public Diveal() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 12;
        setMonsterData(MonsterEnum.DIVEAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CardCounterPatches.lastAttacker instanceof AbstractMonster && !CardCounterPatches.lastAttacker.isDeadOrEscaped()) {
            dmg(CardCounterPatches.lastAttacker, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            if (magicNumber > 0) {
                Wiz.applyToEnemy((AbstractMonster) CardCounterPatches.lastAttacker, new TapeJamPower(CardCounterPatches.lastAttacker, magicNumber));
            }
        }
    }

    public void triggerOnGlowCheck() {
        if (CardCounterPatches.lastAttacker == null || CardCounterPatches.lastAttacker.isDeadOrEscaped()) {
            this.glowColor = Settings.RED_TEXT_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
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
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
        baseMagicNumber = magicNumber = 0;
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.DIVEBERG);
    }

    public void upgrade1() {
        upgradeDamage(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.SCUBALRUS);
    }
}