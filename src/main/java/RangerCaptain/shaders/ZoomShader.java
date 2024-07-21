package RangerCaptain.shaders;

import RangerCaptain.MainModfile;
import basemod.interfaces.ScreenPostProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.nio.charset.StandardCharsets;

public class ZoomShader implements ScreenPostProcessor {
    private static final ZoomShader singleton = new ZoomShader();
    private final ShaderProgram sp = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(MainModfile.makePath("shaders/zoom.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    private float zoom;
    private float x, y;

    @Override
    public void postProcess(SpriteBatch sb, TextureRegion textureRegion, OrthographicCamera orthographicCamera) {
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
        ShaderProgram back = sb.getShader();
        sb.setShader(sp);
        sp.setUniformf("zoom", 1/zoom);
        sp.setUniformf("u_mouse", x, y);
        sb.draw(textureRegion, 0, 0);
        sb.setShader(back);
    }

    public static ZoomShader get(float zoom, float x, float y) {
        singleton.zoom = zoom;
        singleton.x = x;
        singleton.y = y;
        return singleton;
    }

    public static void updateZoom(float zoom) {
        singleton.zoom = zoom;
    }

    public static void updatePosition(float x, float y) {
        singleton.x = x;
        singleton.y = y;
    }
}
