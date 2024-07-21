package RangerCaptain.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SpotlightEnemyEffect extends AbstractGameEffect {
    private final float DUR = 3.0f;
    private final float HALF_DUR = DUR/2f;
    private final float desiredCX;
    private float crimp = 1f;

    public SpotlightEnemyEffect(AbstractCreature c) {
        this.duration = DUR;
        this.color = new Color(1.0F, 1.0F, 0.8F, 0.5F);
        this.desiredCX = c.hb.cX;
    }

    public void update() {
        if (this.duration == DUR) {
            CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.6F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > HALF_DUR) {
            this.color.a = Interpolation.pow5In.apply(0.5F, 0.0F, (this.duration - HALF_DUR) / HALF_DUR);
            crimp = Interpolation.pow5In.apply(0.4f, 1f, (this.duration - HALF_DUR)/HALF_DUR);
        } else {
            this.color.a = Interpolation.exp10In.apply(0.0F, 0.5F, this.duration / HALF_DUR);
        }

        if (this.duration < 0.0F) {
            this.color.a = 0.0F;
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.SPOTLIGHT_VFX, desiredCX-(crimp*Settings.WIDTH/4f), 0.0F, crimp *Settings.WIDTH/2f, Settings.HEIGHT);
        sb.setBlendFunction(770, 771);
    }// 44

    public void dispose() {
    }// 48
}