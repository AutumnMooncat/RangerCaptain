package RangerCaptain.util;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;

import java.nio.charset.StandardCharsets;

public class FusionController {
    public static ShaderProgram fusionProgram = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(MainModfile.makePath("shaders/fusion.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    public static final boolean RENDER_TEST = true;
    public static MonsterEnum monster1 = MonsterEnum.ANATHEMA;
    public static MonsterEnum monster2 = MonsterEnum.ADEPTILE;
    public static FusionForm fusedForm = new FusionForm(monster1, monster2);

    public static void renderTest(SpriteBatch sb) {
        renderNodes(sb, fusedForm);
    }

    public static void renderNodes(SpriteBatch sb, FusionForm form) {
        Vector2 topLeft = new Vector2(0, 0);
        Vector2 bottomRight = new Vector2(0, 0);
        for (FusionNodeData.Node node : form.nodes) {
            if (node != null) {
                topLeft = new Vector2(Math.min(topLeft.x, node.position.x), Math.min(topLeft.y, node.position.y));
                bottomRight = new Vector2(Math.max(bottomRight.x, node.position.x + node.children[0].animation.getKeyFrame(MainModfile.time).getRegionWidth()), Math.max(bottomRight.y, node.position.y));
            }
        }
        ShaderProgram backup = sb.getShader();
        sb.setShader(fusionProgram);
        for (int i = 0 ; i < 15 ; i++) {
            fusionProgram.setUniformf("swap_from_"+i, PaletteData.FUSION_BASE[i]);
            fusionProgram.setUniformf("swap_to_"+i, form.palette[i]);
        }
        for (FusionNodeData.Node node : form.nodes) {
            if (node != null && node.visible) {
                for (FusionNodeData.Node child : node.children) {
                    TextureRegion region = child.animation.getKeyFrame(MainModfile.time);
                    float scale = 5f;
                    sb.draw(
                            region,
                            Settings.WIDTH/2f + node.position.x - scale*(bottomRight.x - topLeft.x)/2f,
                            Settings.HEIGHT/2f - region.getRegionHeight() + node.position.y + scale*(bottomRight.y - topLeft.y)/2f,
                            0 - node.position.x,
                            region.getRegionHeight() - node.position.y,
                            region.getRegionWidth(),
                            region.getRegionHeight(),
                            Settings.scale * scale, Settings.scale * scale, 0
                    );
                }
            }
        }
        sb.setShader(backup);
    }
}
