package org.test.editor.core.service;


import org.test.editor.core.dto.FileVersionDTO;
import org.test.editor.core.model.FileVersion;

import java.util.List;

public interface FileVersionService {
    FileVersion createFileVersion(FileVersion fileVersion, Integer userId);
    List<FileVersionDTO> getFileVersionsByFileId(Integer fileId, int limit);
}