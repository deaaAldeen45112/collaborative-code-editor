package org.test.editor.core.service;
import org.test.editor.core.dto.CreateFileDTO;
import org.test.editor.core.dto.FileDTO;
import org.test.editor.core.dto.UpdateFileDTO;
import org.test.editor.core.model.File;

import java.util.List;

public interface FileService {
    void editFileByUser(UpdateFileDTO updateFileDTO, Integer userId);
    File createFileByUser(CreateFileDTO createFileDTO, Integer userId);
    List<FileDTO> getAllFiles();
    String getFileById(Integer fileId);
    void deleteFile(Integer fileId);
}