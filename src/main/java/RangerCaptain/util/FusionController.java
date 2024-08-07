package RangerCaptain.util;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;

import java.nio.charset.StandardCharsets;

public class FusionController {
    public static ShaderProgram fusionProgram = new ShaderProgram(SpriteBatch.createDefaultShader().getVertexShaderSource(), Gdx.files.internal(MainModfile.makePath("shaders/fusion.frag")).readString(String.valueOf(StandardCharsets.UTF_8)));
    public static final boolean RENDER_TEST = false;
    public static MonsterEnum monster1 = MonsterEnum.POMBOMB;
    public static MonsterEnum monster2 = MonsterEnum.AVEREVOIR;
    public static FusionForm fusedForm = new FusionForm(monster1, monster2);

    public static void renderTest(SpriteBatch sb) {
        renderNodes(sb, fusedForm, Settings.WIDTH/2f, Settings.HEIGHT/2f, 0, 0, 5, 0);
    }

    public static void renderNodes(SpriteBatch sb, FusionForm form, float drawX, float drawY, float displacementX, float displacementY, float scale, float rotation) {
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
                    sb.draw(
                            region,
                            drawX + node.position.x - form.positionalOffset.x + displacementX,
                            drawY - region.getRegionHeight() + node.position.y - form.positionalOffset.y + displacementY,
                            0 - node.position.x + form.positionalOffset.x - displacementX,
                            region.getRegionHeight() - node.position.y + form.positionalOffset.y - displacementY,
                            region.getRegionWidth(),
                            region.getRegionHeight(),
                            Settings.scale * scale, Settings.scale * scale, rotation
                    );
                }
            }
        }
        sb.setShader(backup);
    }
}
