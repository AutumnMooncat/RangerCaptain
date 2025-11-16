package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.MultitargetComponent;
import RangerCaptain.cardfusion.components.vfx.DieDieDieVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;

import static RangerCaptain.MainModfile.makeID;

public class Kuneko extends AbstractEasyCard {
    public final static String ID = makeID(Kuneko.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.KUNEKO)
                .withCost(3)
                .with(new DieDieDieVFXComponent())
                .withDamage(15, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new MultitargetComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.SHINING_KUNEKO)
                .withCost(3)
                .with(new DieDieDieVFXComponent())
                .withDamage(20, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new MultitargetComponent(1))
                .register();
    }

    public Kuneko() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 22;
        baseMagicNumber = magicNumber = 1;
        //isMultiDamage = true;
        setMonsterData(MonsterEnum.KUNEKO);
        setElementalType(ElementalType.AIR);
        tags.add(CustomTags.AOE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //int[] alive = {0};
        //addToBot(new DoAction(() -> alive[0] = Wiz.getEnemies().size()));
        addToBot(new VFXAction(new BorderLongFlashEffect(Color.LIGHT_GRAY)));
        addToBot(new VFXAction(new DieDieDieEffect(), 0.7F));
        addToBot(new ShakeScreenAction(0.0F, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        //allDmg(AbstractGameAction.AttackEffect.SLASH_HEAVY);
        /*addToBot(new DoAction(() -> {
            for (int i = 0; i < alive[0] - Wiz.getEnemies().size(); i++) {
                addToTop(new GainEnergyAction(magicNumber));
            }
        }));*/
        Wiz.applyToSelf(new MultitargetPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(8);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SHINING_KUNEKO);
        setElementalType(ElementalType.ASTRAL);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(PURPLE), WHITE, darken(PURPLE), WHITE, false);
    }
}