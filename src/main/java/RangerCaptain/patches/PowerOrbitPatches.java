package RangerCaptain.patches;

import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.ColoredPotionParticleEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

public class PowerOrbitPatches {
    private static final float ORBIT_RADIUS = 150f * Settings.scale;
    private static final float BASE_ANGLE_SPEED = 25f;
    private static final float BASE_INTERVAL = 0.075f;
    private static float particleTimer;
    private static float angle;
    private static final ArrayList<Color> colors = new ArrayList<>();

    @SpirePatch2(clz = AbstractPlayer.class, method = "render")
    public static class RenderFloatyElementIcons {
        @SpirePrefixPatch
        public static void patch(AbstractPlayer __instance, SpriteBatch sb) {
            particleTimer -= Gdx.graphics.getRawDeltaTime();
            angle -= BASE_ANGLE_SPEED*Gdx.graphics.getDeltaTime();
            angle %= 360;
            if (particleTimer <= 0f) {
                particleTimer = BASE_INTERVAL;
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof OrbitPower) {
                        for (int i = 0 ; i < ((OrbitPower) p).orbAmount() ; i++) {
                            colors.add(((OrbitPower) p).orbColor());
                        }
                    }
                }
                float da = 360f / colors.size();
                float a = 0;
                for (Color c : colors) {
                    Vector2 dir = new Vector2(ORBIT_RADIUS, 0);
                    dir.rotate(angle + a);
                    AbstractDungeon.effectsQueue.add(new ColoredPotionParticleEffect(__instance.hb.cX + dir.x, __instance.hb.cY + dir.y, c, 10f, 0.1f));
                    a += da;
                }
                colors.clear();
            }
        }
    }

    public interface OrbitPower {
        int orbAmount();
        Color orbColor();
    }
}
