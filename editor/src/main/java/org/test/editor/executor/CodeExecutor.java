package org.test.editor.executor;

import java.io.IOException;

public interface CodeExecutor {
    Process execute(String directoryPath,String scriptPath) throws IOException;
}