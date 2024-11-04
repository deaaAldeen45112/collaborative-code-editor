package org.test.editor.executor;

import org.springframework.stereotype.Component;
import org.test.editor.util.constant.ProjectTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class CodeExecutorFactory {
    private final Map<ProjectTemplate, CodeExecutor> executors = new HashMap<>();

    public CodeExecutorFactory() {
        executors.put(ProjectTemplate.C_PLUS, new CplusConsole());
    }

    public CodeExecutor getExecutor(ProjectTemplate language) {
        return executors.get(language);
    }
}