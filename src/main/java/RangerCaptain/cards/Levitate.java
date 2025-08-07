package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.damageMods.effects.LightningOrbVFX;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.powers.NextTurnTakeDamagePower;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Levitate extends AbstractEasyCard {
    public final static String ID = makeID(Levitate.class.getSimpleName());

    public Levitate() {
        super(ID, 1, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 1;
        DamageModifierManager.addModifier(this, new LightningOrbVFX());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useJumpAnimation();
        Wiz.applyToEnemy(m, new NextTurnTakeDamagePower(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        Wiz.applyToSelf(new APBoostPower(p, magicNumber));
    }

    @Override
    public void upp() {
        //upgradeBaseCost(0);
        upgradeMagicNumber(1);
    }
}