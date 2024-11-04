package org.test.editor.infra.service;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Service
public class ZipService {

    public File zipFolder(String folderPath) throws IOException {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Provided path is not a valid directory");
        }


        File zipFile = File.createTempFile("folder-", ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            zipFolderContents(folder, folder.getName(), zos);
        }
        return zipFile;
    }

    private void zipFolderContents(File folder, String zipEntryName, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            ZipEntry emptyDirEntry = new ZipEntry(zipEntryName + "/");
            zos.putNextEntry(emptyDirEntry);
            zos.closeEntry();
        } else {
            for (File file : files) {
                if (file.isDirectory()) {
                    zipFolderContents(file, zipEntryName + "/" + file.getName(), zos);
                } else {
                    zipFile(file, zipEntryName, zos);
                }
            }
        }
    }


    private void zipFile(File file, String zipEntryName, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(zipEntryName + "/" + file.getName());
            zos.putNextEntry(zipEntry);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }
}
