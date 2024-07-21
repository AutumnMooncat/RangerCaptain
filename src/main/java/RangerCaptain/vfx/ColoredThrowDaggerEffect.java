package RangerCaptain.vfx;

import RangerCaptain.util.CustomLighting;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;

public class ColoredThrowDaggerEffect extends ThrowDaggerEffect implements CustomLighting {
    public ColoredThrowDaggerEffect(float x, float y, Color c) {
        super(x, y);
        color.set(c);
    }

    public ColoredThrowDaggerEffect(float x, float y, float fAngle, Color c) {
        super(x, y, fAngle);
        color.set(c);
    }

    @Override
    public float[] _lightsOutGetXYRI() {
        return new float[] {
                ReflectionHacks.<Float>getPrivate(this, ThrowDaggerEffect.class, "x") + (startingDuration - duration)*5 * Settings.WIDTH, ReflectionHacks.getPrivate(this, ThrowDaggerEffect.class, "y"), 50f * duration/startingDuration, 1.0f
        };
    }

    @Override
    public Color[] _lightsOutGetColor() {
        return new Color[] {
                color
        };
    }
}
