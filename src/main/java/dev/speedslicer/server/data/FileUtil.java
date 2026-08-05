package dev.speedslicer.server.data;

import java.nio.file.Path;

public class FileUtil {
    public static boolean isJsonFile(Path path) {
        return path.getFileName()
                .toString()
                .toLowerCase()
                .endsWith(".json");
    }

}
