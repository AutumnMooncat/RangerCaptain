package RangerCaptain.damageMods.effects;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;

import static RangerCaptain.MainModfile.makeID;

public class ScrapeVFX extends AbstractDamageModifier {
    public final static String ID = makeID(ScrapeVFX.class.getSimpleName());

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.effectList.add(new ScrapeEffect(target.hb.cX, target.hb.cY));
    }

    @Override
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        return new ScrapeVFX();
    }
}
