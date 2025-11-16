package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.FocusedComponent;
import RangerCaptain.cardfusion.components.vfx.InflameVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.FocusedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import static RangerCaptain.MainModfile.makeID;

public class Khepri extends AbstractEasyCard {
    public final static String ID = makeID(Khepri.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.KHEPRI)
                .withCost(0)
                .withDamage(4.5f, AbstractGameAction.AttackEffect.FIRE)
                .with(new InflameVFXComponent())
                .with(new FocusedComponent(1))
                .register();
    }

    public Khepri() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.KHEPRI);
        setElementalType(ElementalType.FIRE);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new VFXAction(p, new InflameEffect(p), 0.1F));
        Wiz.applyToSelf(new FocusedPower(p, magicNumber));
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