package org.dirs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.stream.Stream;

public class OrganizeIntoDirs {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java OrganizeIntoDirs <input-dir>");
            System.exit(0);
        }

        File sourceDir = new File(args[0]);
        if (!sourceDir.isDirectory()) {
            System.err.println("Input directory is not a directory.");
            System.exit(-1);
        }
        Stream.of(Objects.requireNonNull(sourceDir.listFiles(new JpegFileFilter()))).forEach(OrganizeIntoDirs::processFile);
    }

    private static void processFile(File file) {
        String currentDirPath = file.getParent();
        String targetDirName = getDateShot(file);

        File targetDir = new File(currentDirPath + File.separator + targetDirName);
        if (!targetDir.exists()) {
            boolean dirCreated = targetDir.mkdirs();
            if (!dirCreated) {
                System.err.println("Failed to create directory " + targetDirName);
            } else {
                System.out.println("Directory " + targetDirName + " did not exist. It is now created.");
            }
        }
        try {
            moveFileToTargetDir(file, targetDir);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private static String getDateShot(File file) {
        return "";
    }

    private static void moveFileToTargetDir(File file, File targetDir) throws IOException {
        Path sourcePath = file.toPath();
        Path targetPath = Paths.get(targetDir.getPath(), file.getName());
        System.out.println("Moving " + file + " to " + targetDir);
        Files.move(sourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);
    }
}
