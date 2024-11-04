package org.test.editor.util;

public class FilePathExtractor {

    public static String extractProjectName(String filePath) {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        String normalizedPath = filePath.trim().replaceAll("/{2,}", "/");
        String[] parts = normalizedPath.split("/");
        if (parts.length >= 3) {
            return parts[2];
        }
        throw new IllegalArgumentException("Invalid file path: Project name not found in the expected position");
    }
}
