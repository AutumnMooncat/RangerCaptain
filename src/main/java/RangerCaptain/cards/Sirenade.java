package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.WeakComponent;
import RangerCaptain.cardfusion.components.vfx.PiercingWailVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static RangerCaptain.MainModfile.makeID;

public class Sirenade extends AbstractEasyCard {
    public final static String ID = makeID(Sirenade.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SIRENADE)
                .withCost(1)
                .with(new PiercingWailVFXComponent())
                .withDamageAOE(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new WeakComponent(0.91f, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.DECIBELLE)
                .withCost(1)
                .with(new PiercingWailVFXComponent())
                .withDamageAOE(5.5f, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new WeakComponent(1.91f, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
    }

    public Sirenade() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 1;
        isMultiDamage = true;
        setMonsterData(MonsterEnum.SIRENADE);
        tags.add(CustomTags.AOE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
        addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.PURPLE_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
        allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new WeakPower(mon, magicNumber, false)));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.DECIBELLE);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, PURPLE, WHITE, PURPLE, WHITE, false);
    }
}