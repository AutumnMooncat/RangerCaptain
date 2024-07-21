package RangerCaptain.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.WaterSplashParticleEffect;

public class ColoredWaterSplashEffect extends WaterSplashParticleEffect {
    public ColoredWaterSplashEffect(float x, float y, Color c) {
        super(x, y);
        color.set(c);
    }
}
