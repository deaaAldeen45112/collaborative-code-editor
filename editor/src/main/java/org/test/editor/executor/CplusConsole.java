package org.test.editor.executor;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.editor.presentation.websocket.handler.CodeRunWebSocketHandler;
import org.test.editor.util.PathHelper;

import static org.test.editor.util.PathHelper.rootLocation;

public class CplusConsole implements CodeExecutor {

    private static final Logger logger = LoggerFactory.getLogger(CodeRunWebSocketHandler.class);

    @Override
    public Process execute(String directoryPath, String scriptPath) throws IOException {
        var projectPath=rootLocation.resolve(directoryPath).toAbsolutePath().toString();
        ProcessBuilder processBuilder = new ProcessBuilder(scriptPath, projectPath);
        return processBuilder.start();
    }
}
