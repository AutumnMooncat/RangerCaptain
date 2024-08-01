package RangerCaptain.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

public class TSCNProcessor {
    public static final boolean SHOULD_PROCESS = true;
    private static final BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter("TSCNOutput.log", false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void process() {
        Path dir = Paths.get("F:\\Downloads\\GDRE_tools-v0.6.2-windows\\CB Unpack\\.import");
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

    private static void parseFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            ArrayList<String> foundValues = new ArrayList<>();
            boolean isFusion = false;
            while (scanner.hasNextLine()) {
                String scannedLine = scanner.nextLine();
                if (scannedLine.startsWith("[ext_resource path=\"res://sprites/fusions")) {
                    writer.append(parseSpritePath(scannedLine));
                    writer.append(", ");
                    isFusion = true;
                }
                if (isFusion) {
                    if (scannedLine.startsWith("\"values\":")) {
                        foundValues.add(parseValues(scannedLine));
                    } else if (scannedLine.startsWith("anims/idle =")) {
                        int i = parseIndex(scannedLine);
                        if (i != -1 && foundValues.size() > i) {
                            writer.append(foundValues.get(i));
                            writer.newLine();
                        }
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseSpritePath(String line) {
        return line.replace("[ext_resource path=\"res://sprites/","\"").replace("\" type=\"Texture\" id=1]","\"");
    }

    private static String parseValues(String line) {
        return line.replace("\"values\": [ ","").replace(" ]","").replaceAll("Rect2","new Rect2");
    }

    private static int parseIndex(String line) {
        int ret = 0;
        try {
            ret = Integer.parseInt(line.replace("anims/idle = SubResource( ","").replace(" )",""));
        } catch (NumberFormatException ignored) {}
        return ret-1;
    }
}
