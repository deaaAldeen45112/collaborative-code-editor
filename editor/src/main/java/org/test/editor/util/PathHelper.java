package org.test.editor.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PathHelper {
    public static final Path rootLocation = Paths.get("/app/code-editor-storage");

    public static String generateNewArchivePath(String archivePath) {
        var timestamp = String.valueOf(System.currentTimeMillis());
        return archivePath + "_" + timestamp;
    }

    public static String constructProjectPath(String username, String projectName) {
        return constructCodePath(username) + "/" + projectName;
    }

    public static String getNewCodePath(String archivePath, String projectName) {
        return archivePath + "/" + projectName;
    }

    public static String constructFolderPath(String basePath, String folderName) {
        return basePath + "/" + folderName;
    }

    public static String constructFilePath(String basePath, String fileName) {
        return basePath + "/" + fileName;
    }

    public static String constructCodePath(String username) {
        return username + "/projects/code";
    }

    public static String constructProjectArchivePath(String username, String projectName) {
        return username + "/projects/archive/" + projectName;
    }
    public static String constructProjectVersionPath(String username, String projectName) {
        return username + "/projects/version/" + projectName;
    }
    public static String constructFileVersionsPath(String projectVersionPath, String fileName) {
        String result = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;
        return projectVersionPath + "/" + result + "_" + UUID.randomUUID() ;
    }
    public static String constructFileVersionPath(String fileVersionPath, String fileName) {
        String baseName = fileName.contains(".") ? fileName.substring(0, fileName.indexOf('.')) : fileName;
        return fileVersionPath + "/" + baseName + "_" + UUID.randomUUID() + (fileName.contains(".") ? fileName.substring(fileName.indexOf('.')) : "");
    }

}