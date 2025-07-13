package RangerCaptain.damageMods.effects;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import static RangerCaptain.MainModfile.makeID;

public class LightningOrbVFX extends AbstractDamageModifier {
    public final static String ID = makeID(LightningOrbVFX.class.getSimpleName());
    public static final float COOLDOWN = 0.05f;

    public LightningOrbVFX() {
        MainModfile.sfxCooldowns.put(ID, COOLDOWN);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        addToBot(new VFXAction(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY)));
        if (MainModfile.sfxCooldowns.get(ID) <= 0f) {
            addToBot(new SFXAction("ORB_LIGHTNING_CHANNEL", 0.2f));
            MainModfile.sfxCooldowns.put(ID, COOLDOWN);
        }
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new LightningOrbVFX();
    }
}
