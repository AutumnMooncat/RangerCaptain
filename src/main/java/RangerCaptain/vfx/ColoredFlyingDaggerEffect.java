package RangerCaptain.vfx;

import RangerCaptain.util.CustomLighting;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.FlyingDaggerEffect;

public class ColoredFlyingDaggerEffect extends FlyingDaggerEffect implements CustomLighting {
    public ColoredFlyingDaggerEffect(float x, float y, float fAngle, boolean shouldFlip, Color c) {
        super(x, y, fAngle, shouldFlip);
        color.set(c);
        color.a = 0f;
    }

    @Override
    public float[] _lightsOutGetXYRI() {
        return new float[] {
                ReflectionHacks.getPrivate(this, FlyingDaggerEffect.class, "x"), ReflectionHacks.getPrivate(this, FlyingDaggerEffect.class, "y"), 50f, 1.0f
        };
    }

    @Override
    public Color[] _lightsOutGetColor() {
        return new Color[] {
                color
        };
    }
}