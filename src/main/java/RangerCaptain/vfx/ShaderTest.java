package RangerCaptain.vfx;

import RangerCaptain.MainModfile;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.nio.charset.StandardCharsets;

public class ShaderTest implements ScreenPostProcessor {
    public static ShaderProgram sp = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(MainModfile.makePath("shaders/sparks.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    public static float time;

    @Override
    public void postProcess(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera orthographicCamera) {
        time += Gdx.graphics.getRawDeltaTime();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        ShaderProgram back = sb.getShader();
        sb.setShader(sp);
        sp.setUniformf("x_time", time);
        sp.setUniformf("u_mouse", InputHelper.mX, InputHelper.mY);
        sb.draw(textureRegion, 0, 0);
        sb.setShader(back);
    }
}
