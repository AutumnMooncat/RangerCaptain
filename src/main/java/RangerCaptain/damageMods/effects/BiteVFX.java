package RangerCaptain.damageMods.effects;

import RangerCaptain.MainModfile;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static RangerCaptain.MainModfile.makeID;

public class BiteVFX extends AbstractDamageModifier {
    public final static String ID = makeID(BiteVFX.class.getSimpleName());
    public static final float COOLDOWN = 0.05f;

    public BiteVFX() {
        MainModfile.sfxCooldowns.put(ID, COOLDOWN);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        addToTop(new VFXAction(new BiteEffect(target.hb.cX, target.hb.cY, Settings.GOLD_COLOR.cpy()), 0.1F));
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new BiteVFX();
    }
}
