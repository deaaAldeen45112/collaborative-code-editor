package org.test.editor.infra.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;

@Service
public class TempFileCleanupService {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

    @Scheduled(fixedRate = 3600000)
    public void cleanupOldTempFiles() {
        File tempDir = new File(TEMP_DIR);
        File[] tempFiles = tempDir.listFiles((dir, name) -> name.startsWith("folder-") && name.endsWith(".zip"));

        if (tempFiles != null) {
            for (File file : tempFiles) {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    Instant fileCreationTime = attrs.creationTime().toInstant();
                    if (Instant.now().minusSeconds(3600).isAfter(fileCreationTime)) {
                        file.delete();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

