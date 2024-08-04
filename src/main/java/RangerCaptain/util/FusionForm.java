package RangerCaptain.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class FusionForm {
    public MonsterEnum monster1;
    public MonsterEnum monster2;
    public FusionNodeData.Node[] nodes;
    public Color[] palette;

    public FusionForm(MonsterEnum monster1, MonsterEnum monster2) {
        this.monster1 = monster1;
        this.monster2 = monster2;
        this.nodes = blendNodes(monster1, monster2);
        this.palette = getPalette(monster1, monster2);
    }

    public FusionNodeData.Node[] blendNodes(MonsterEnum monster1, MonsterEnum monster2) {
        FusionNodeData.Node[] positionNodes1 = FusionNodeData.NODE_DATA.get(monster1);
        FusionNodeData.Node[] positionNodes2 = FusionNodeData.NODE_DATA.get(monster2);
        HashMap<String, Boolean> choices = new HashMap<>();
        ArrayList<FusionNodeData.Node> chosenNodes = new ArrayList<>();

        for (FusionNodeData.Node node : positionNodes1) {
            boolean useFirst = node.forceUsage || MathUtils.randomBoolean();
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
            FusionNodeData.Node copy = getNamedNode(positionNodes1, positionNodes2, choices.get(node.nodeName), node.nodeName);
            copy.position = node.position;
            copy.visible = node.visible;
            chosenNodes.add(copy);
        }

        return chosenNodes.toArray(new FusionNodeData.Node[0]);
    }

    public FusionNodeData.Node getNamedNode(FusionNodeData.Node[] nodes, String name) {
        for (FusionNodeData.Node node : nodes) {
            if (node.nodeName.equals(name)) {
                return node;
            }
        }
        return null;
    }

    public FusionNodeData.Node getNamedNode(FusionNodeData.Node[] nodes1, FusionNodeData.Node[] nodes2, boolean useFirst, String name) {
        return getNamedNode(useFirst ? nodes1 : nodes2, name);
    }

    public FusionNodeData.Node[] getRenderNodes(FusionNodeData.Node[] nodes, String name) {
        for (FusionNodeData.Node node : nodes) {
            if (node.nodeName.equals(name)) {
                return node.children;
            }
        }
        return new FusionNodeData.Node[0];
    }

    public FusionNodeData.Node[] getRenderNodes(FusionNodeData.Node[] nodes1, FusionNodeData.Node[] nodes2, boolean useFirst, String name) {
        return getRenderNodes(useFirst ? nodes1 : nodes2, name);
    }

    public Color[] getPalette(MonsterEnum monster1, MonsterEnum monster2) {
        ArrayList<Color> chosenColors = new ArrayList<>();
        boolean useFirst = MathUtils.randomBoolean();
        for (int i = 0 ; i < 15 ; i++) {
            chosenColors.add(useFirst ? PaletteData.PALETTE_DATA.get(monster1)[i] : PaletteData.PALETTE_DATA.get(monster2)[i]);
            if (i == 4 || i == 9) {
                useFirst = !useFirst;
            }
        }
        return chosenColors.toArray(new Color[0]);
    }
}
