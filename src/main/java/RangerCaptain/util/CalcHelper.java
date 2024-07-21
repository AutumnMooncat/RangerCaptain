package RangerCaptain.util;

import RangerCaptain.MainModfile;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CalcHelper {
    private static final DummyCard dummyCard = new DummyCard();

    public static int applyPowers(int damage) {
        dummyCard.setMultiDamage(false);
        dummyCard.baseDamage = damage;
        dummyCard.applyPowers();
        return dummyCard.damage;
    }

    public static int[] applyPowersMulti(int damage) {
        dummyCard.setMultiDamage(true);
        dummyCard.baseDamage = damage;
        dummyCard.applyPowers();
        return dummyCard.multiDamage;
    }

    public static int applyPowersToBlock(int block) {
        dummyCard.baseBlock = block;
        dummyCard.applyPowers();
        return dummyCard.block;
    }

    public static int calculateCardDamage(int damage, AbstractMonster mo) {
        dummyCard.setMultiDamage(false);
        dummyCard.baseDamage = damage;
        dummyCard.calculateCardDamage(mo);
        return dummyCard.damage;
    }

    public static int[] calculateCardDamageMulti(int damage) {
        dummyCard.setMultiDamage(true);
        dummyCard.baseDamage = damage;
        dummyCard.calculateCardDamage(null);
        return dummyCard.multiDamage;
    }

    public static class DummyCard extends CustomCard {
        public static final String ID = MainModfile.makeID("DummyCard");

        public DummyCard() {
            super(ID, "Name", "images/cards/locked_attack.png", 1, "Description", CardType.ATTACK, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.ALL);
            baseDamage = damage = 1;
            baseBlock = block = 1;
            baseMagicNumber = magicNumber = 1;
        }

        public void setMultiDamage(boolean var) {
            this.isMultiDamage = var;
        }

        @Override
        public void upgrade() {}

        @Override
        public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {}
    }
}
