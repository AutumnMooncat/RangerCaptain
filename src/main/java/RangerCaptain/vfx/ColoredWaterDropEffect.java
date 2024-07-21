package RangerCaptain.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.vfx.combat.WaterDropEffect;

public class ColoredWaterDropEffect extends WaterDropEffect {
    private float x;
    private float y;
    private int frame = 0;
    private float animTimer = 0.1F;
    public ColoredWaterDropEffect(float x, float y, Color c) {
        super(x, y);
        this.x = x;
        this.y = y;
        color.set(c);
        scale /= Settings.scale;
    }

    @Override
    public void update() {
        this.color.a = MathHelper.fadeLerpSnap(this.color.a, 1.0F);// 30
        this.animTimer -= Gdx.graphics.getDeltaTime();// 31
        if (this.animTimer < 0.0F) {// 32
            this.animTimer += 0.1F;// 33
            ++this.frame;// 34
            if (this.frame == 3) {// 36
                for(int i = 0; i < 3; ++i) {// 37
                    AbstractDungeon.effectsQueue.add(new ColoredWaterSplashEffect(this.x, this.y, color));// 38
                }
            }

            if (this.frame > 5) {// 42
                this.frame = 5;// 43
                this.isDone = true;// 44
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);// 50
        switch (this.frame) {// 51
            case 0:
                sb.draw(ImageMaster.WATER_DROP_VFX[0], this.x - 32.0F, this.y - 32.0F + 40.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 53
                break;// 70
            case 1:
                sb.draw(ImageMaster.WATER_DROP_VFX[1], this.x - 32.0F, this.y - 32.0F + 20.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 72
                break;// 89
            case 2:
                sb.draw(ImageMaster.WATER_DROP_VFX[2], this.x - 32.0F, this.y - 32.0F + 10.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 91
                break;// 108
            case 3:
                sb.draw(ImageMaster.WATER_DROP_VFX[3], this.x - 32.0F, this.y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 110
                break;// 127
            case 4:
                sb.draw(ImageMaster.WATER_DROP_VFX[4], this.x - 32.0F, this.y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 129
                break;// 146
            case 5:
                sb.draw(ImageMaster.WATER_DROP_VFX[5], this.x - 32.0F, this.y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.rotation, 0, 0, 64, 64, false, false);// 148
        }

    }
}
