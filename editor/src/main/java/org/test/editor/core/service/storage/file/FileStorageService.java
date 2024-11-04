package org.test.editor.core.service.storage.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String createFile(String fileName,String filePath) throws IOException;
    String upload(MultipartFile file, String path) throws IOException;
    boolean delete(String filePath) throws IOException;
    boolean move(String oldFilePath, String newFilePath) throws IOException;
    void edit(String filePath, String newContent) throws IOException;
    void createAndWriteToFile(String filePath, String newContent) throws IOException;
    String read(String filePath) throws IOException;
}