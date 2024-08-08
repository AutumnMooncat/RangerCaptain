package RangerCaptain.util;

import RangerCaptain.MainModfile;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FusionForm {
    private final Random random;
    public MonsterEnum monster1;
    public MonsterEnum monster2;
    public FusionNodeData.Node[] nodes;
    public Color[] palette;
    public String fusionName;
    public Vector2 positionalOffset;
    public float width, height;

    public FusionForm(MonsterEnum monster1, MonsterEnum monster2) {
        this.random = new Random((monster1.toString()+monster2.toString()).hashCode());
        this.monster1 = monster1;
        this.monster2 = monster2;
        this.nodes = blendNodes(monster1, monster2);
        this.palette = getPalette(monster1, monster2);
        if (monster1 == monster2) {
            String affix = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("SameFusionAffix")).TEXT[random.nextInt(10)];
            //Check if it's a suffix instead
            if (affix.startsWith(" ")) {
                fusionName = monster1.toString().replace("_", "") + affix;
            } else {
                fusionName = affix + monster1.toString().replace("_"," ");
            }
        } else {
            fusionName = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(monster1+"Fusion")).TEXT[0] + CardCrawlGame.languagePack.getUIString(MainModfile.makeID(monster2+"Fusion")).TEXT[1];
        }
        Rectangle boundingBox = getBoundingBox(nodes);
        this.positionalOffset = getPositionalOffset(nodes);
        this.width = boundingBox.getWidth();
        this.height = boundingBox.getHeight();
    }

    private FusionNodeData.Node[] blendNodes(MonsterEnum monster1, MonsterEnum monster2) {
        FusionNodeData.Node[] positionNodes1 = FusionNodeData.NODE_DATA.get(monster1);
        FusionNodeData.Node[] positionNodes2 = FusionNodeData.NODE_DATA.get(monster2);
        HashMap<String, Boolean> choices = new HashMap<>();
        ArrayList<FusionNodeData.Node> chosenNodes = new ArrayList<>();

        for (FusionNodeData.Node node : positionNodes1) {
            boolean useFirst = node.forceUsage || random.nextBoolean();
            FusionNodeData.Node[] children = getRenderNodes(positionNodes1, positionNodes2, useFirst, node.nodeName);
            if (children.length == 0) {
                useFirst = !useFirst;
            }
            choices.put(node.nodeName, useFirst);
        }

        for (FusionNodeData.Node node : positionNodes1) {
            if (!node.matchPart.isEmpty()) {
                FusionNodeData.Node matchPart = getNamedNode(positionNodes1, node.matchPart);
                if (matchPart != null) {
                    if (node.inverseMatch) {
                        choices.put(node.nodeName, !choices.get(matchPart.nodeName));
                    } else {
                        choices.put(node.nodeName, choices.get(matchPart.nodeName));
                    }
                    if (getRenderNodes(positionNodes1, positionNodes2, choices.get(node.nodeName), node.nodeName).length == 0) {
                        choices.put(node.nodeName, !choices.get(node.nodeName));
                    }
                }
            }
        }

        for (FusionNodeData.Node node : positionNodes1) {
            FusionNodeData.Node copy = getNamedNode(positionNodes1, positionNodes2, choices.get(node.nodeName), node.nodeName).makeCopy();
            copy.position = node.position;
            copy.visible = node.visible;
            chosenNodes.add(copy);
        }

        return chosenNodes.toArray(new FusionNodeData.Node[0]);
    }

    private FusionNodeData.Node getNamedNode(FusionNodeData.Node[] nodes, String name) {
        for (FusionNodeData.Node node : nodes) {
            if (node.nodeName.equals(name)) {
                return node;
            }
        }
        return null;
    }

    private FusionNodeData.Node getNamedNode(FusionNodeData.Node[] nodes1, FusionNodeData.Node[] nodes2, boolean useFirst, String name) {
        return getNamedNode(useFirst ? nodes1 : nodes2, name);
    }

    private FusionNodeData.Node[] getRenderNodes(FusionNodeData.Node[] nodes, String name) {
        for (FusionNodeData.Node node : nodes) {
            if (node.nodeName.equals(name)) {
                return node.children;
            }
        }
        return new FusionNodeData.Node[0];
    }

    private FusionNodeData.Node[] getRenderNodes(FusionNodeData.Node[] nodes1, FusionNodeData.Node[] nodes2, boolean useFirst, String name) {
        return getRenderNodes(useFirst ? nodes1 : nodes2, name);
    }

    private Color[] getPalette(MonsterEnum monster1, MonsterEnum monster2) {
        ArrayList<Color> chosenColors = new ArrayList<>();
        boolean useFirst = random.nextBoolean();
        for (int i = 0 ; i < 15 ; i++) {
            chosenColors.add(useFirst ? PaletteData.PALETTE_DATA.get(monster1)[i] : PaletteData.PALETTE_DATA.get(monster2)[i]);
            if (i == 4 || i == 9) {
                useFirst = !useFirst;
            }
        }
        return chosenColors.toArray(new Color[0]);
    }

    public Vector2 getPositionalOffset(FusionNodeData.Node[] nodeArray) {
        Vector2 topLeft = new Vector2(999, 999);
        Vector2 bottomRight = new Vector2(-999, -999);
        for (FusionNodeData.Node node : nodeArray) {
            if (node != null) {
                topLeft = new Vector2(Math.min(topLeft.x, node.position.x), Math.min(topLeft.y, node.position.y - node.children[0].animation.getKeyFrame(0).getRegionHeight()));
                bottomRight = new Vector2(Math.max(bottomRight.x, node.position.x + node.children[0].animation.getKeyFrame(0).getRegionWidth()), Math.max(bottomRight.y, node.position.y));
            }
        }
        return new Vector2((bottomRight.x + topLeft.x)/2f, (bottomRight.y + topLeft.y)/2f);
    }

    private Rectangle getBoundingBox(FusionNodeData.Node[] nodeArray) {
        Vector2 topLeft = new Vector2(999, 999);
        Vector2 bottomRight = new Vector2(-999, -999);
        for (FusionNodeData.Node node : nodeArray) {
            if (node != null) {
                topLeft = new Vector2(Math.min(topLeft.x, node.position.x), Math.min(topLeft.y, node.position.y - node.children[0].animation.getKeyFrame(0).getRegionHeight()));
                bottomRight = new Vector2(Math.max(bottomRight.x, node.position.x + node.children[0].animation.getKeyFrame(0).getRegionWidth()), Math.max(bottomRight.y, node.position.y));
            }
        }
        return new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
    }
}
