package org.test.editor.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.test.editor.core.service.FileVersionService;

@RestController
@RequestMapping("/api/file-versions")
public class FileVersionController {

    private final FileVersionService fileVersionService;

    @Autowired
    public FileVersionController(FileVersionService fileVersionService) {
        this.fileVersionService = fileVersionService;
    }



}
