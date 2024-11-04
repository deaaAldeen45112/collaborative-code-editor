package org.test.editor.infra.service.storage.file;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.test.editor.core.service.storage.file.FileStorageService;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
public class LocalFileStorageService implements FileStorageService {
    private final Path rootLocation = Paths.get("code-editor-storage");
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>() {{
        add(".cpp");
        add(".py");
    }};
    private final ConcurrentHashMap<String, Lock> fileLocks = new ConcurrentHashMap<>();

    public String createFile(String fileName,String filePath) throws IOException {
        validateFileExtension(fileName);
        try {
            Path fullPath = rootLocation.resolve(filePath).resolve(fileName);
            if (!Files.exists(fullPath)) {
                return Files.createFile(fullPath).toString();
            } else {
                throw new IOException("File already exists: " + filePath);
            }
        } catch (IOException e) {
            throw new IOException("Failed to create file: " + filePath +", "+e.getMessage());
        }
    }

    @Override
    public String upload(MultipartFile file, String path) throws IOException {
        validateFileExtension(file.getOriginalFilename());
        try {
            Path directory = rootLocation.resolve(path).normalize();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            Path localFile = directory.resolve(file.getOriginalFilename());
            file.transferTo(localFile);
            return localFile.toString();
        } catch (IOException e) {
            throw new IOException("Failed to upload file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public boolean delete(String filePath) throws IOException{
        try {
            Path file = Paths.get(filePath);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new IOException("Failed to delete file: " + filePath, e);
        }
    }

    @Override
    public boolean move(String oldPath, String newPath) throws IOException {
        try {
            Path oldFullPath = rootLocation.resolve(oldPath);
            Path newFullPath = rootLocation.resolve(newPath);
            if (Files.exists(oldFullPath)) {
                Files.move(oldFullPath, newFullPath, StandardCopyOption.ATOMIC_MOVE);
                return Files.exists(newFullPath) && !Files.exists(oldFullPath);
            }
            return false;
        } catch (IOException e) {
            throw new IOException("Failed to rename file from: " + oldPath + " to: " + newPath, e);
        }
    }

    @Override
    public void edit(String filePath, String newContent)  throws IOException{

        try {
            Path fullPath = rootLocation.resolve(filePath);
            Files.writeString(fullPath, newContent);
        } catch (IOException e) {
            throw new IOException("Failed to edit file: " + filePath, e);
        }
    }

    @Override
    public void createAndWriteToFile(String filePath, String newContent) throws IOException {
        try {
            Path fullPath = rootLocation.resolve(filePath);
            Files.writeString(fullPath, newContent, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new IOException("Failed to edit file: " + filePath, e);
        }
    }

    @Override
    public String read(String filePath) throws IOException {
        try {
            Path fullPath = rootLocation.resolve(filePath);
            return Files.readAllLines(fullPath).stream().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new IOException("Failed to read file: " + filePath, e);
        }
    }
    private void validateFileExtension(String fileName) throws IOException {boolean isValid = ALLOWED_EXTENSIONS.stream().anyMatch(fileName.toLowerCase()::endsWith);
        if (!isValid) {
            throw new IOException("File type not allowed: " + fileName + ". Only " + String.join(", ", ALLOWED_EXTENSIONS) + " are allowed.");
        }
    }
}
