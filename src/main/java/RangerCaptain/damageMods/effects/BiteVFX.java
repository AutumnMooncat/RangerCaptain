package RangerCaptain.damageMods.effects;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static RangerCaptain.MainModfile.makeID;

public class BiteVFX extends AbstractDamageModifier {
    public final static String ID = makeID(BiteVFX.class.getSimpleName());

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.effectList.add(new BiteEffect(target.hb.cX, target.hb.cY, Settings.GOLD_COLOR.cpy()));
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
