package org.dirs;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Stream;

public class OrganizeIntoDirs {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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

        processFilesInDirectory(sourceDir.getAbsolutePath());
    }

    private static void processFilesInDirectory(String sourceDirPath) {
        long start = System.currentTimeMillis();
        System.out.println("Start processing: " + sourceDirPath);
        try (Stream<Path> stream = Files.list(Paths.get(sourceDirPath))) {
            stream.filter(filePath -> !Files.isDirectory(filePath))
                    .map(filePath -> new File(filePath.toString()))
                    .forEach(OrganizeIntoDirs::processFile);
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }
        long end = System.currentTimeMillis();
        System.out.println("OrganizeIntoDirs took " + (end - start) + "ms");
    }

    private static void processFile(File file) {
        String currentDirPath = file.getParent();
        String targetDirName = getDirectoryNameFromDateShot(file);
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

    private static String getDirectoryNameFromDateShot(File file) {
        String dirName = "";
        try {
            dirName = JpegMetadataExtractor.readJpegDateTaken(file);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        dirName = dirName.replace(":","-").replace("'","").substring(0, 10);
        try {
            validateDirName(dirName);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return dirName;
    }

    private static void moveFileToTargetDir(File file, File targetDir) throws IOException {
        Path sourcePath = file.toPath();
        Path targetPath = Paths.get(targetDir.getPath(), file.getName());
        System.out.println("Moving " + file + " to " + targetDir);
        Files.move(sourcePath, targetPath, StandardCopyOption.ATOMIC_MOVE);
    }

    private static void validateDirName(String dirName) throws ParseException {
        SIMPLE_DATE_FORMAT.parse(dirName);
    }
}
