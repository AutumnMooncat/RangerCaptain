package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.damageMods.BootDamage;
import RangerCaptain.damageMods.PiercingDamage;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterData;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Bansheep extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Bansheep.class.getSimpleName());

    public Bansheep() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 10;
        baseMagicNumber = magicNumber = 0;
        setMonsterData(MonsterData.BANSHEEP);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (magicNumber > 0) {
            for (int i = 0 ; i < magicNumber ; i++) {
                dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            }
        } else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(IRIS), WHITE, darken(IRIS), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        addUpgradeData(this::upgrade3, 2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        upgradeDamage(3);
        DamageModifierManager.addModifier(this, new BootDamage(CardModifierManager.modifiedBaseValue(this, baseDamage, "D")));
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterData.WOOLTERGEIST);
        baseInfo = info = CardModifierManager.modifiedBaseValue(this, baseDamage, "D");
        baseSecondMagic = secondMagic = info - 1;
    }

    public void upgrade1() {
        upgradeDamage(4);
        DamageModifierManager.modifiers(this).forEach(mod -> {
            if (mod instanceof BootDamage) {
                ((BootDamage) mod).amount = CardModifierManager.modifiedBaseValue(this, baseDamage, "D");
            }
        });
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterData.RAMTASM);
        baseInfo = info = CardModifierManager.modifiedBaseValue(this, baseDamage, "D");
        upgradedInfo = true;
        baseSecondMagic = secondMagic = info - 1;
        upgradedSecondMagic = true;
    }

    public void upgrade2() {
        upgradeDamage(-4);
        upgradeMagicNumber(2);
        DamageModifierManager.addModifier(this, new PiercingDamage());
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        initializeTitle();
        setMonsterData(MonsterData.ZOMBLEAT);
    }

    public void upgrade3() {
        upgradeDamage(-1);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[3];
        initializeTitle();
        setMonsterData(MonsterData.CAPRICORPSE);
    }
}