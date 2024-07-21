package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.EchoMod;
import RangerCaptain.util.ImageHelper;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.nio.charset.StandardCharsets;

public class ShaderOnCardPatches {
    public static ShaderProgram sp = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(MainModfile.makePath("shaders/echo.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    public static FrameBuffer fb = ImageHelper.createBuffer();
    private static boolean capturing;

    public static void capture(SpriteBatch sb) {
        if (!capturing) {
            capturing = true;
            sb.end();
            ImageHelper.beginBuffer(fb);
            sb.begin();
        }
    }

    public static void draw(SpriteBatch sb) {
        if (capturing) {
            capturing = false;
            sb.end();
            fb.end();
            sb.begin();
            TextureRegion r = ImageHelper.getBufferTexture(fb);
            ShaderProgram origShader = sb.getShader();
            Color origColor = sb.getColor();
            sb.setShader(sp);
            sp.setUniformf("x_time", MainModfile.time);
            sb.setColor(Color.WHITE);
            sb.draw(r, 0, 0);
            sb.setShader(origShader);
            sb.setColor(origColor);
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "renderInLibrary")
    public static class ApplyShaderInLibrary {
        @SpirePrefixPatch
        public static void apply(AbstractCard __instance, SpriteBatch sb) {
            if (CardModifierManager.hasModifier(__instance, EchoMod.ID)) {
                capture(sb);
            }
        }

        @SpirePostfixPatch
        public static void remove(AbstractCard __instance, SpriteBatch sb) {
            draw(sb);
        }
    }

    @SpirePatch2(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class ApplyShader {
        @SpirePrefixPatch
        public static void apply(AbstractCard __instance, SpriteBatch sb) {
            if (CardModifierManager.hasModifier(__instance, EchoMod.ID)) {
                capture(sb);
            }
        }

        @SpirePostfixPatch
        public static void remove(AbstractCard __instance, SpriteBatch sb) {
            draw(sb);
        }
    }
}
