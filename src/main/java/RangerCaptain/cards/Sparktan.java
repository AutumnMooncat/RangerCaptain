package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddConductiveDamageComponent;
import RangerCaptain.cardfusion.components.vfx.LightningFVXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.damageMods.ConductiveDamage;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static RangerCaptain.MainModfile.makeID;

public class Sparktan extends AbstractEasyCard {
    public final static String ID = makeID(Sparktan.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SPARKTAN)
                .withCost(2)
                .withDamage(6.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningFVXComponent())
                .with(new AddConductiveDamageComponent())
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.ZEUSTRIKE)
                .withCost(2)
                .withDamage(8, AbstractGameAction.AttackEffect.NONE)
                .with(new LightningFVXComponent())
                .with(new AddConductiveDamageComponent())
                .withExhaust()
                .register();
    }

    public Sparktan() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
        setMonsterData(MonsterEnum.SPARKTAN);
        setElementalType(ElementalType.LIGHTNING);
        exhaust = true;
        DamageModifierManager.addModifier(this, new ConductiveDamage());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new SFXAction("THUNDERCLAP", 0.05F));
            addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.05F));
        }
        dmg(m, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        tags.add(CardTags.STRIKE);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.ZEUSTRIKE);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(GOLD), WHITE, darken(GOLD), WHITE, false);
    }
}