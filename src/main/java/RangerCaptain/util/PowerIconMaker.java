package RangerCaptain.util;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerIconMaker {
    public static void setIcons(AbstractPower p, Texture t, boolean smaller) {
        int s = 48;
        int l = 128;
        if (smaller) {
            s = 32;
            l = 86;
        }
        Texture small = TextureScaler.rescale(t, s, s);
        Texture large = TextureScaler.rescale(t, l, l);
        p.region48 = new TextureAtlas.AtlasRegion(small, 0, 0, small.getWidth(), small.getHeight());
        p.region128 = new TextureAtlas.AtlasRegion(large, 0, 0, large.getWidth(), large.getHeight());
    }

    public static void setIcons(AbstractPower p, String itemCode) {
        setIcons(p, itemCode, false);
    }

    public static void setIcons(AbstractPower p, String itemCode, boolean smaller) {
        setIcons(p, TexLoader.getTexture(MainModfile.makeImagePath("items/"+ itemCode +".png")), smaller);
    }
}
