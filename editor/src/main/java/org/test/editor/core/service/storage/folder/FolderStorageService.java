package org.test.editor.core.service.storage.folder;
import java.io.IOException;

public interface FolderStorageService {
    void create(String folderPath) throws IOException;
    void createFoldersPath(String folderPath) throws IOException;
    boolean delete(String folderPath) throws IOException;
    boolean move(String oldFolderPath,  String newContent) throws IOException;
    public void copyInnerFolders(String sourcePath, String targetPath) throws IOException;
}