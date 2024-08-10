package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;

import static RangerCaptain.MainModfile.makeID;

public class Dominoth extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Dominoth.class.getSimpleName());

    public Dominoth() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.DOMINOTH);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            Wiz.applyToEnemy(m, new ConductivePower(m, p, magicNumber));
        }
        addToBot(new DrawCardAction(magicNumber));
        if (info == 2) {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
        addToBot(new DiscardAction(p, p, 1, false));
    }

    @Override
    public void applyPowers() {
        baseDamage = -1;
        if (info == 2) {
            baseDamage = (int) Wiz.adp().hand.group.stream().filter(c -> c != this).count();
            int cardsAvailable = Wiz.adp().drawPile.size() + Wiz.adp().discardPile.size();
            if (Wiz.adp().hasPower(NoDrawPower.POWER_ID)) {
                cardsAvailable = 0;
            }
            baseDamage += Math.min(magicNumber, cardsAvailable);
            baseDamage = Math.min(baseDamage, BaseMod.MAX_HAND_SIZE);
        }
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = -1;
        if (info == 2) {
            baseDamage = (int) Wiz.adp().hand.group.stream().filter(c -> c != this).count();
            int cardsAvailable = Wiz.adp().drawPile.size() + Wiz.adp().discardPile.size();
            if (Wiz.adp().hasPower(NoDrawPower.POWER_ID)) {
                cardsAvailable = 0;
            }
            baseDamage += Math.min(magicNumber, cardsAvailable);
            baseDamage = Math.min(baseDamage, BaseMod.MAX_HAND_SIZE);
        }
        super.calculateCardDamage(mo);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(darken(BLUE)), WHITE, darken(darken(BLUE)), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1, 0);
        addUpgradeData(this::upgrade2);
        setExclusions(0,2);
    }

    public void upgrade0() {
        target = CardTarget.ENEMY;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WINGLOOM);
        baseInfo = info = 1;
        tags.add(CustomTags.MAGIC_CONDUCTIVE);
    }

    public void upgrade1() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.MOTHMANIC);
    }

    public void upgrade2() {
        baseDamage = 0;
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[2];
        type = CardType.ATTACK;
        target = CardTarget.ENEMY;
        initializeTitle();
        setMonsterData(MonsterEnum.TOKUSECT);
        baseInfo = info = 2;
        rollerKey += "Attack";
        needsArtRefresh = true;
    }
}