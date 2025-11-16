package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.TapeJamComponent;
import RangerCaptain.cardfusion.components.vfx.ScrapeVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.interfaces.ManuallySizeAdjustedCard;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;

import static RangerCaptain.MainModfile.makeID;

public class Ferriclaw extends AbstractEasyCard implements ManuallySizeAdjustedCard {
    public final static String ID = makeID(Ferriclaw.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.FERRICLAW)
                .withCost(1)
                .withDamage(8.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new ScrapeVFXComponent(), new TapeJamComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.AURICLAW)
                .withCost(1)
                .withDamage(11.5f, AbstractGameAction.AttackEffect.NONE)
                .with(new ScrapeVFXComponent(), new TapeJamComponent(1))
                .register();
    }

    public Ferriclaw() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 12;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.FERRICLAW);
        setElementalType(ElementalType.METAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new ScrapeEffect(m.hb.cX, m.hb.cY), 0.1F));
        }
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        Wiz.applyToEnemy(m, new TapeJamPower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.AURICLAW);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }

    @Override
    public float getAdjustedScale() {
        return 0.975f;
    }
}