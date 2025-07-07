package RangerCaptain.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class TRESPaletteDataProcessor {
    public static final boolean SHOULD_PROCESS = false;
    private static final BufferedWriter writer;

    static {
        try {
            writer = new BufferedWriter(new FileWriter("PaletteOutput.log", false));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void process() {
        Path dir = Paths.get("F:\\Downloads\\GDRE_tools-v0.6.2-windows\\CB Unpack\\data\\monster_forms");
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.forEach(path -> {
                if (path.toString().endsWith(".tres")) {
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
            while (scanner.hasNextLine()) {
                String scannedLine = scanner.nextLine();
                if (scannedLine.startsWith("name = \"")) {
                    writer.append("add(").append(scannedLine.replace("name = \"", "").replace("_NAME\"", ""));
                    writer.append(", ");
                }
                if (scannedLine.startsWith("swap_colors =")) {
                    writer.append(scannedLine.replace("swap_colors = [ ","").replace("]",");"));
                    writer.newLine();
                }
            }
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
