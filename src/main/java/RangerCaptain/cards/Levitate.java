package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import static RangerCaptain.MainModfile.makeID;

public class Levitate extends AbstractEasyCard {
    public final static String ID = makeID(Levitate.class.getSimpleName());

    public Levitate() {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        baseDamage = damage = 3;
        baseMagicNumber = magicNumber = 1;
        tags.add(CustomTags.MAGIC_VULN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.useJumpAnimation();
        if (m != null) {
            addToBot(new VFXAction(new LightningOrbActivateEffect(m.hb.cX, m.hb.cY)));
        }
        addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        Wiz.applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
    }
}