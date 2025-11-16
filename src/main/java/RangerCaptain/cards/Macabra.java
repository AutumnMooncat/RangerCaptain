package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AbramacabraComponent;
import RangerCaptain.cardfusion.components.vfx.BiteVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.AbramacabraPower;
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

public class Macabra extends AbstractEasyCard {
    public final static String ID = makeID(Macabra.class.getSimpleName());

    static {
        // 7,2 -> 11,3
        new FusionComponentHelper(MonsterEnum.MACABRA)
                .withCost(1)
                .withDamage(5.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new BiteVFXComponent())
                .with(new AbramacabraComponent(1.91f))
                .register();
        // 9,3 -> 14,5
        new FusionComponentHelper(MonsterEnum.FOLKLORD)
                .withCost(1)
                .withDamage(7, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new AbramacabraComponent(2.91f))
                .register();
    }

    public Macabra() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.MACABRA);
        setElementalType(ElementalType.BEAST);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (info == 0) {
            if (m != null) {
                addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.1F));
            }
            dmg(m, AbstractGameAction.AttackEffect.NONE);
        } else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }
        Wiz.applyToSelf(new AbramacabraPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FOLKLORD);
        baseInfo = info = 1;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }
}