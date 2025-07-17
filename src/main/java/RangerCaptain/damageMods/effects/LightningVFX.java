package RangerCaptain.damageMods.effects;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static RangerCaptain.MainModfile.makeID;

public class LightningVFX extends AbstractDamageModifier {
    public final static String ID = makeID(LightningVFX.class.getSimpleName());
    public static final float COOLDOWN = 0.05f;

    public LightningVFX() {
        MainModfile.sfxCooldowns.put(ID, COOLDOWN);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        addToTop(new VFXAction(new LightningEffect(target.hb.cX, target.hb.cY)));
        addToTop(new SFXAction("THUNDERCLAP", 0.2f));
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new LightningVFX();
    }
}
