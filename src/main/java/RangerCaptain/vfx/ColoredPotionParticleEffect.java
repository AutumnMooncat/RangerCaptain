package RangerCaptain.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;

public class ColoredPotionParticleEffect extends UncommonPotionParticleEffect {
    public ColoredPotionParticleEffect(float x, float y, Color c) {
        this(x, y, c, 25f, 6f);
    }

    public ColoredPotionParticleEffect(float x, float y, Color c, float scatter, float impulse) {
        super(x, y);
        color.set(c.r, c.g, c.b, 0);
        float oX = MathUtils.random(-scatter, scatter) * Settings.scale;
        float oY = MathUtils.random(-scatter, scatter) * Settings.scale;
        oX -= ImageMaster.GLOW_SPARK_2.packedWidth / 2.0F;
        oY -= ImageMaster.GLOW_SPARK_2.packedHeight / 2.0F;
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "oX", oX);
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "oY", oY);

        float vX = MathUtils.random(-impulse, impulse) * Settings.scale;
        float vY = MathUtils.random(-impulse, impulse) * Settings.scale;
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "vX", vX);
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "vY", vY);
    }

    public ColoredPotionParticleEffect(Hitbox hb, Color c) {
        this(hb, c, 25f, 6f);
    }

    public ColoredPotionParticleEffect(Hitbox hb, Color c, float scatter, float impulse) {
        super(hb);
        color.set(c.r, c.g, c.b, 0);
        float oX = MathUtils.random(-scatter, scatter) * Settings.scale;
        float oY = MathUtils.random(-scatter, scatter) * Settings.scale;
        oX -= ImageMaster.GLOW_SPARK_2.packedWidth / 2.0F;
        oY -= ImageMaster.GLOW_SPARK_2.packedHeight / 2.0F;
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "oX", oX);
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "oY", oY);

        float vX = MathUtils.random(-impulse, impulse) * Settings.scale;
        float vY = MathUtils.random(-impulse, impulse) * Settings.scale;
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "vX", vX);
        ReflectionHacks.setPrivateInherited(this, ColoredPotionParticleEffect.class, "vY", vY);
    }
}
