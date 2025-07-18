package RangerCaptain.damageMods.effects;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;

import static RangerCaptain.MainModfile.makeID;

public class LightningOrbVFX extends AbstractDamageModifier {
    public final static String ID = makeID(LightningOrbVFX.class.getSimpleName());

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.effectList.add(new LightningOrbActivateEffect(target.hb.cX, target.hb.cY));
        CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.2f);
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
