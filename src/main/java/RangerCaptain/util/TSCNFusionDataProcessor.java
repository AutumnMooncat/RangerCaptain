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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void parseFile(File file) {
        try {
            String name = FormatHelper.capitalize(file.getName().replace(".tscn",""));
            BufferedWriter writer = new BufferedWriter(new FileWriter("fusiondata\\"+name+".java", false));

            //Write initial code block
            writer.append("package RangerCaptain.fusionData;");
            writer.newLine();
            writer.newLine();
            writer.append("import RangerCaptain.util.FusionData;");
            writer.newLine();
            writer.append("import com.badlogic.gdx.math.Vector2;");
            writer.newLine();
            writer.newLine();
            writer.append("public class ").append(name).append(" extends FusionData {");
            writer.newLine();
            writer.append("\tpublic ").append(name).append("() {");
            writer.newLine();
            writer.append("\t\tsuper(");

            //Grab node stuff
            Scanner scanner = new Scanner(file);
            HashMap<Integer, String> assetMap = new HashMap<>();
            int foundNodes = 0;
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
                    writer.append("new Node(");
                } else if (line.startsWith("visible = false")) {
                    writer.append("false, ");
                } else if (line.startsWith("position =") && foundNodes < 11) {
                    writer.append(line.replace("position =","new")).append(", ");
                } else if (line.startsWith("force_usage =")) {
                    writer.append("true, ");
                } else if (line.endsWith(" )]")) {
                    String[] chunks = line.split(" ");
                    int index = Integer.parseInt(chunks[chunks.length-2]);
                    writer.append("\"").append(assetMap.get(index)).append("\")");
                    if (foundNodes < 10) {
                        writer.append(", ");
                    }
                    foundNodes++;
                }
            }

            //Finalize file
            writer.append(");");
            writer.newLine();
            writer.append("\t}");
            writer.newLine();
            writer.append("}");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
