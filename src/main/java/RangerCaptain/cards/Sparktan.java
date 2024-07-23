package RangerCaptain.cards;

import RangerCaptain.actions.DamageFollowupAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.ConductivePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static RangerCaptain.MainModfile.makeID;

public class Sparktan extends AbstractEasyCard {
    public final static String ID = makeID(Sparktan.class.getSimpleName());
    protected static Animation<TextureRegion> sparktan = loadGifOverlay("Sparktan_idle.gif");
    protected static Animation<TextureRegion> zeustrike = loadGifOverlay("Zeustrike_idle.gif");

    public Sparktan() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 5;
        gifOverlay = sparktan;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new SFXAction("THUNDERCLAP", 0.05F));
            addToBot(new VFXAction(new LightningEffect(m.drawX, m.drawY), 0.05F));
        }
        addToBot(new DamageFollowupAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE, false, mon -> {
            if (mon instanceof AbstractMonster) {
                Wiz.applyToEnemy((AbstractMonster) mon, new ConductivePower(mon, p, mon.lastDamageTaken));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        tags.add(CardTags.STRIKE);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        gifOverlay = zeustrike;
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