package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Faucetear extends AbstractEasyCard {
    public final static String ID = makeID(Faucetear.class.getSimpleName());

    static {
        // 5x3 -> 7x5
        new FusionComponentHelper(MonsterEnum.FAUCETEAR)
                .withCost(2)
                .withMultiDamageAOE(3, 3, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .withExhaust()
                .register();
        // 5x4 -> 7x7 or 7x3 -> 10x5
        new FusionComponentHelper(MonsterEnum.FOUNTESS)
                .withCost(2)
                .withMultiDamageAOE(4, 3, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .withExhaust()
                .register();
    }

    public Faucetear() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 3;
        isMultiDamage = true;
        exhaust = true;
        setMonsterData(MonsterEnum.FAUCETEAR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            if (i == magicNumber - 1) {
                allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            } else {
                allDmg(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            }
        }
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        //upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FOUNTESS);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }
}