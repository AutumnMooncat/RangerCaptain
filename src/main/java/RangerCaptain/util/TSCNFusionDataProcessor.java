package RangerCaptain.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

public class TSCNFusionDataProcessor {
    public static final boolean SHOULD_PROCESS = false;
    private static final BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter("FusionDataOutput.log", false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final String[] NODE_KEYS = new String[] {
            "[node name=\"Arm_Back\"",
            "[node name=\"Tail\"",
            "[node name=\"FrontLeg_Back\"",
            "[node name=\"BackLeg_Back\"",
            "[node name=\"Body\"",
            "[node name=\"FrontLeg_Front\"",
            "[node name=\"BackLeg_Front\"",
            "[node name=\"HelmetBack\"",
            "[node name=\"Head\"",
            "[node name=\"HelmetFront\"",
            "[node name=\"Arm_Front\""
    };

    public static void process() {
        Path dir = Paths.get("F:\\Downloads\\GDRE_tools-v0.6.2-windows\\CB Unpack\\data\\fusions");
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(path -> {
                if (path.toString().endsWith(".tscn")) {
                    parseFile(path.toFile());
                }
            });
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void parseFile(File file) {
        try {
            String name = FormatHelper.capitalize(file.getName().replace(".tscn",""));
            writer.append("add(").append(name.toUpperCase());

            //Grab node stuff
            Scanner scanner = new Scanner(file);
            HashMap<Integer, String> assetMap = new HashMap<>();
            int nodeLayer = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("[ext_resource path=\"res://sprites/")) {
                    String pathAndIndex = line.replace("[ext_resource path=\"res://sprites/","").replace("\" type=\"PackedScene\" id=","").replace("]","");
                    try {
                        int index = Integer.parseInt(pathAndIndex.substring(pathAndIndex.length()-2));
                        String path = pathAndIndex.substring(0, pathAndIndex.length()-2).replace(".json",".gif");
                        assetMap.put(index, path);
                    } catch (NumberFormatException ignored) {
                        String path = pathAndIndex.substring(0, pathAndIndex.length()-1).replace(".json",".gif");
                        int index = Integer.parseInt(pathAndIndex.substring(pathAndIndex.length()-1));
                        assetMap.put(index, path);
                    }
                } else if (Arrays.stream(NODE_KEYS).anyMatch(line::startsWith)) {
                    if (nodeLayer > 1) {
                        writer.append(".build()).build()");
                        nodeLayer = 0;
                    }
                    writer.append(", new NodeBuilder(").append(line.replace("[node name=","").replace(" type=\"Node2D\" parent=\".\"]",")"));
                    nodeLayer++;
                } else if (line.startsWith("visible = false")) {
                    writer.append(".setVisible(false)");
                } else if (line.startsWith("position =") && nodeLayer > 0) {
                    writer.append(".setPosition(").append(line.replace("position =","new")).append(")");
                    if (nodeLayer > 1) {
                        writer.append(".build())");
                        nodeLayer--;
                    }
                } else if (line.startsWith("force_usage = true")) {
                    writer.append(".setForceUsage(true)");
                } else if (line.startsWith("match_part =")) {
                    writer.append(line.replace("match_part = NodePath(\"../",".setMatchPart(\""));
                } else if (line.startsWith("inverse_match = true")) {
                    writer.append(".setInverseMatch(true)");
                } else if (line.endsWith(" )]")) {
                    if (nodeLayer > 1) {
                        writer.append(".build())");
                        nodeLayer--;
                    }
                    String[] chunks = line.split(" ");
                    int index = Integer.parseInt(chunks[chunks.length-2]);
                    writer.append(".addChild(new NodeBuilder(\"").append(line.replace("[node name=\"","").replaceAll("\".*","\")"));
                    nodeLayer++;
                    writer.append(".setTexturePath(\"").append(assetMap.get(index)).append("\")");
                } else if (line.startsWith("[node name=\"attack\"")) {
                    writer.append(".build()).build());");
                    nodeLayer = 0;
                }
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
