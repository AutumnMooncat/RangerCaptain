package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.MultitargetComponent;
import RangerCaptain.cardfusion.components.vfx.PiercingWailVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.MultitargetPower;
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
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import static RangerCaptain.MainModfile.makeID;

public class Sirenade extends AbstractEasyCard {
    public final static String ID = makeID(Sirenade.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SIRENADE)
                .withCost(1)
                .with(new PiercingWailVFXComponent())
                .withDamageAOE(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new MultitargetComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.DECIBELLE)
                .withCost(1)
                .with(new PiercingWailVFXComponent())
                .withDamageAOE(7.5f, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new MultitargetComponent(1))
                .register();
    }

    public Sirenade() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 7;
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
        Wiz.applyToSelf(new MultitargetPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
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