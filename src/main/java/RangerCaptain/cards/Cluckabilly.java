package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.PetrifiedComponent;
import RangerCaptain.cardfusion.components.vfx.BiteVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.PetrifiedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static RangerCaptain.MainModfile.makeID;

public class Cluckabilly extends AbstractEasyCard {
    public final static String ID = makeID(Cluckabilly.class.getSimpleName());

    static {
        // 7,1 -> 11,2
        new FusionComponentHelper(MonsterEnum.CLUCKABILLY)
                .withCost(1)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new PetrifiedComponent(1))
                .register();
        // 8,1 -> 12,3
        new FusionComponentHelper(MonsterEnum.ROCKERTRICE)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.NONE)
                .with(new BiteVFXComponent())
                .with(new PetrifiedComponent(1.91f))
                .register();
    }

    public Cluckabilly() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 1;
        baseInfo = info = 0;
        setMonsterData(MonsterEnum.CLUCKABILLY);
        setElementalType(ElementalType.AIR);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 1) {
            if (m != null) {
                addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.1F));
            }
            dmg(m, AbstractGameAction.AttackEffect.NONE);
        } else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        }
        Wiz.applyToEnemy(m, new PetrifiedPower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.ROCKERTRICE);
        baseInfo = info = 1;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BRONZE), WHITE, darken(BRONZE), WHITE, false);
    }
}