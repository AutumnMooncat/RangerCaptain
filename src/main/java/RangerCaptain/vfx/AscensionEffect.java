package RangerCaptain.vfx;

import RangerCaptain.util.Wiz;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class AscensionEffect extends AbstractGameEffect {
    public AscensionEffect() {
        this.duration = 3.0F;// 15
        this.color = new Color(1.0F, 1.0F, 0.8F, 0.5F);// 16
    }// 17

    public void update() {
        /*if (this.duration == 3.0F) {// 20
            CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.6F);// 21
        }*/

        this.duration -= Gdx.graphics.getDeltaTime();// 24
        if (this.duration > 1.5F) {// 26
            this.color.a = Interpolation.pow5In.apply(0.5F, 0.0F, (this.duration - 1.5F) / 1.5F);// 27
        } else {
            this.color.a = Interpolation.pow5In.apply(0.0F, 0.5F, this.duration / 1.5F);// 29
        }

        if (this.duration < 0.0F) {// 32
            this.color.a = 0.0F;// 33
            this.isDone = true;// 34
        }

    }// 36

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);// 40
        sb.setBlendFunction(770, 1);// 41
        sb.draw(ImageMaster.SPOTLIGHT_VFX, Wiz.adp().hb.cX-Wiz.adp().hb.width*1.5f, 0.0F, Wiz.adp().hb.width*3f, (float)Settings.HEIGHT);
        sb.setBlendFunction(770, 771);// 43
    }// 44

    public void dispose() {
    }// 48
}
