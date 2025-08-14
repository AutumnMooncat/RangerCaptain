package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddBoostAlreadyAttackedDamageComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Icepeck extends AbstractEasyCard {
    public final static String ID = makeID(Icepeck.class.getSimpleName());

    static {
        // 8,2 -> 11,8
        new FusionComponentHelper(MonsterEnum.ICEPECK)
                .withCost(2)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new AddBoostAlreadyAttackedDamageComponent(4))
                .register();
        // 10,3 -> 14,10
        new FusionComponentHelper(MonsterEnum.CRYOSHEAR)
                .withCost(2)
                .withDamage(7, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new AddBoostAlreadyAttackedDamageComponent(5))
                .register();
    }

    public Icepeck() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.ICEPECK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int base = baseDamage;
        baseDamage += magicNumber * CardCounterPatches.AttackCountField.attackedThisCombat.get(mo);
        super.calculateCardDamage(mo);
        baseDamage = base;
        isDamageModified = baseDamage != damage;
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.CRYOSHEAR);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(RED), WHITE, darken(RED), WHITE, false);
    }
}