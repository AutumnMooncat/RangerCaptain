package RangerCaptain.vfx;

import RangerCaptain.util.CustomLighting;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ParticleEffect extends AbstractGameEffect implements CustomLighting {
    protected float x;
    protected float y;
    protected float oX;
    protected float oY;
    protected float vX;
    protected float vY;
    protected final float dur_div2;
    protected final Hitbox hb;
    protected final TextureAtlas.AtlasRegion img;

    public ParticleEffect(Color c, float x, float y) {
        this(c, (Hitbox)null);// 20
        this.x = x;// 21
        this.y = y;// 22
    }// 23

    public ParticleEffect(Color c, Hitbox hb) {
        this.hb = hb;// 26
        this.img = ImageMaster.GLOW_SPARK_2;// 27
        this.duration = MathUtils.random(1.8F, 2.0F);// 28
        this.scale = MathUtils.random(1.0F, 1.2F) * Settings.scale;// 29
        this.dur_div2 = this.duration / 2.0F;// 30
        this.color = c;
        this.oX = MathUtils.random(-25.0F, 25.0F) * Settings.scale;// 33
        this.oY = MathUtils.random(-25.0F, 25.0F) * Settings.scale;// 34
        this.oX -= (float)this.img.packedWidth / 2.0F;// 35
        this.oY -= (float)this.img.packedHeight / 2.0F;// 36
        this.vX = MathUtils.random(-15.0F, 15.0F) * Settings.scale;// 37
        this.vY = MathUtils.random(-17.0F, 17.0F) * Settings.scale;// 38
        this.renderBehind = MathUtils.randomBoolean(0.2F + (this.scale - 0.5F));// 40
        this.rotation = MathUtils.random(-8.0F, 8.0F);// 41
    }// 42

    public void update() {
        if (this.duration > this.dur_div2) {// 46
            this.color.a = Interpolation.pow3In.apply(0.5F, 0.0F, (this.duration - this.dur_div2) / this.dur_div2);// 47
        } else {
            this.color.a = Interpolation.pow3In.apply(0.0F, 0.5F, this.duration / this.dur_div2);// 49
        }

        this.oX += this.vX * Gdx.graphics.getDeltaTime();// 52
        this.oY += this.vY * Gdx.graphics.getDeltaTime();// 53
        this.duration -= Gdx.graphics.getDeltaTime();// 55
        if (this.duration < 0.0F) {// 56
            this.isDone = true;// 57
        }

    }// 59

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);// 64
        sb.setBlendFunction(770, 1);// 65
        if (this.hb != null) {// 66
            sb.draw(this.img, this.hb.cX + this.oX, this.hb.cY + this.oY, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.8F, 1.2F), this.scale * MathUtils.random(0.8F, 1.2F), this.rotation);// 67 75 76
        } else {
            sb.draw(this.img, this.x + this.oX, this.y + this.oY, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.8F, 1.2F), this.scale * MathUtils.random(0.8F, 1.2F), this.rotation);// 79 87 88
        }

        sb.setBlendFunction(770, 771);// 91
    }// 92

    public void dispose() {
    }// 96

    @Override
    public float[] _lightsOutGetXYRI() {
        if (hb != null) {
            return new float[] {hb.cX + oX, hb.cY + oY, 150f, 0.05f};
        }
        return new float[] {x + oX, y + oY, 150f, 0.05f};
    }

    @Override
    public Color[] _lightsOutGetColor() {
        return new Color[] {color};
    }
}
