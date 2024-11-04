package org.test.editor.infra.service;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.Git;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.editor.core.dto.FileVersionDTO;
import org.test.editor.core.dto.FileVersionWithPathDTO;
import org.test.editor.core.mapper.FileVersionMapper;
import org.test.editor.core.model.FileVersion;
import org.test.editor.core.model.VersionLog;
import org.test.editor.core.service.FileVersionService;
import org.test.editor.core.service.storage.file.FileStorageService;
import org.test.editor.infra.repository.FileVersionRepository;
import org.test.editor.infra.repository.VersionLogRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FileVersionServiceImpl implements FileVersionService {
    private final FileVersionRepository fileVersionRepository;
    private final VersionLogRepository versionLogRepository;
    private final FileStorageService localFileStorageService;
    private final int OPERATION_MAX_LENGTH=2_000;
    private final FileVersionMapper fileVersionMapper;

    public FileVersion createFileVersion(FileVersion fileVersion,Integer userId) {

        var savedFileVersion = fileVersionRepository.save(fileVersion);
        versionLogRepository.save(new VersionLog(
                fileVersion.getVersionId()
                ,userId,
                "edit-file"
        ));
        return savedFileVersion;
    }
    public List<FileVersionDTO> getFileVersionsByFileId(Integer fileId, int limit) {
        Pageable pageable = PageRequest.of(0, 1000);
        var versions=fileVersionRepository.findByFileIdSorted(fileId, pageable);
        try {
            for (FileVersionWithPathDTO version : versions) {
                if (version.getPath() != null) {
                    String fileContent = localFileStorageService.read(version.getPath());
                    version.setDiffContent(fileContent);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return versions.stream()
                .map(fileVersionMapper::toFileVersionDTO)
                .collect(Collectors.toList());
    }
}
