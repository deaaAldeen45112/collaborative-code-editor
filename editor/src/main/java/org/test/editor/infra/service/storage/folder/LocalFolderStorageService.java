package org.test.editor.infra.service.storage.folder;

import org.springframework.stereotype.Service;
import org.test.editor.core.service.storage.folder.FolderStorageService;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

@Service
public class LocalFolderStorageService implements FolderStorageService {
    private final Path rootLocation = Paths.get("code-editor-storage");

    @Override
    public void create(String folderPath) throws IOException {
        try {
            Path targetFolder = rootLocation.resolve(folderPath).normalize();
            Files.createDirectory(targetFolder);
        } catch (IOException e) {
            throw new IOException("Failed to create folder: " + folderPath, e);
        }
    }

    @Override
    public void createFoldersPath(String folderPath) throws IOException {
        try {
            Path targetFolder = rootLocation.resolve(folderPath).normalize();
            Files.createDirectories(targetFolder);
        } catch (IOException e) {
            throw new IOException("Failed to create folder: " + folderPath, e);
        }
    }

    @Override
    public boolean delete(String folderPath) throws IOException {
        try {
            Path targetFolder = rootLocation.resolve(folderPath).normalize();
            if (Files.exists(targetFolder)) {
                Files.walk(targetFolder)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(java.io.File::delete);
                return !Files.exists(targetFolder);
            }
            return false;
        } catch (IOException e) {
            throw new IOException("Failed to delete folder: " + folderPath, e);
        }
    }

    @Override
    public boolean move(String oldFolderPath, String newFolderPath) throws IOException {
        try {
            Path oldFolder = rootLocation.resolve(oldFolderPath).normalize();
            Path newFolder = rootLocation.resolve(newFolderPath).normalize();
            if (Files.exists(oldFolder)) {
                Files.move(oldFolder, newFolder, StandardCopyOption.REPLACE_EXISTING);
                return Files.exists(newFolder) && !Files.exists(oldFolder);
            }
            return false;
        } catch (IOException e) {
            throw new IOException("Failed to move folder from: " + oldFolderPath + " to: " + newFolderPath, e);
        }
    }

    public void copyInnerFolders(String sourcePath, String targetPath) throws IOException {
        Path sourceFolder = rootLocation.resolve(sourcePath).normalize();
        Path targetFolder = rootLocation.resolve(targetPath).normalize();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(sourceFolder)) {
            for (Path innerFolder : directoryStream) {
                if (Files.isDirectory(innerFolder)) {
                    Path newFolder = targetFolder.resolve(innerFolder.getFileName());
                    Files.createDirectories(newFolder);
                    Files.walk(innerFolder)
                            .forEach(source ->
                            {
                                try {
                                    Files.copy(source, newFolder.resolve(innerFolder.relativize(source)), StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }
        }
    }

}
