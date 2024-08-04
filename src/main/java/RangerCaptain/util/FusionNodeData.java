package RangerCaptain.util;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class FusionNodeData {
    public Node[] nodes;

    public FusionNodeData(Node... nodes) {
        this.nodes = nodes;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public static class Node {
        public Animation<TextureRegion> animation;
        public boolean visible;
        public boolean forceUsage;
        public Vector2 position;

        public Node (Vector2 position, String path) {
            this(true, position, false, path);
        }

        public Node(boolean visible, Vector2 position, String path) {
            this(visible, position, false, path);
        }

        public Node(Vector2 position, boolean forceUsage, String path) {
            this(true, position, forceUsage, path);
        }

        public Node(boolean visible, Vector2 position, boolean forceUsage, String path) {
            this.visible = visible;
            this.position = new Vector2(position.x, -position.y); //Godot uses top left (0,0) instead, so we must invert the y coordinate
            this.forceUsage = forceUsage;
            this.animation = loadGif(path);
        }

        private static Animation<TextureRegion> loadGif(String path) {
            return GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(MainModfile.makeImagePath(path)).read());
        }
    }
}
