package RangerCaptain.cards;

import RangerCaptain.actions.EasyXCostAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import java.util.Arrays;

import static RangerCaptain.MainModfile.makeID;

public class Umbrahella extends AbstractEasyCard {
    public final static String ID = makeID(Umbrahella.class.getSimpleName());

    public Umbrahella() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setMonsterData(MonsterEnum.UMBRAHELLA);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 2;
        isMultiDamage = true;
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        tags.add(CustomTags.MAGIC_POISON);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EasyXCostAction(this, (base, params) -> {
            int effect = base + Arrays.stream(params).sum();
            if (effect > 0) {
                for (int i = 0 ; i < multiDamage.length ; i++) {
                    multiDamage[i] *= effect;
                }
                Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemyTop(mon, new PoisonPower(mon, p, magicNumber * effect)));
                allDmgTop(AbstractGameAction.AttackEffect.POISON);
            }
            return true;
        }));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}