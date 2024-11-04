package org.test.editor.presentation.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.core.service.CommentService;
import org.test.editor.core.service.DiscussionService;
import org.test.editor.core.service.ProjectTemplateService;
import org.test.editor.executor.CodeExecutorFactory;
import org.test.editor.infra.repository.ProjectRepository;
import org.test.editor.infra.repository.ProjectTemplateRepository;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;
import org.test.editor.util.constant.CodeRunCommand;
import org.test.editor.util.constant.ProjectTemplate;

import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Component
public class CodeRunCommandHandler extends BaseCommandHandler {
    private final CodeExecutorFactory codeExecutorFactory;
    private final ProjectRepository projectRepository;
    private final ProjectTemplateRepository projectTemplateRepository;
    private static final int CORE_COUNT = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService = Executors.newFixedThreadPool(CORE_COUNT * 4);
    private final ConcurrentHashMap<WebSocketSession, Process> processMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, BufferedWriter> writerMap = new ConcurrentHashMap<>();

    public CodeRunCommandHandler(
            WebSocketMessageBroadcaster broadcaster,
            MessageParser messageParser,
            ProjectRepository projectRepository,
            CodeExecutorFactory codeExecutorFactory,
            ProjectTemplateRepository projectTemplateRepository
    ) {
        super(broadcaster, messageParser);
        this.codeExecutorFactory = codeExecutorFactory;
        this.projectRepository = projectRepository;
        this.projectTemplateRepository = projectTemplateRepository;
    }


    @Override
    public boolean canHandle(String action) {
        return CodeRunCommand.fromString(action) != null;
    }

    @Override
    public void handle(WebSocketSession session, Message<?> message) throws IOException {
        CodeRunCommand command = CodeRunCommand.fromString(message.getAction());
        if (command == null) {
            throw new IllegalArgumentException("Unknown code run command: " + message.getAction());
        }

        switch (command) {
            case RUN -> handleRunCode(session);
            case INPUT -> handleInput(session, message);
        }
    }

    private void handleRunCode(WebSocketSession session) {
        if (processMap.containsKey(session) && processMap.get(session).isAlive()) {
            cleanup(session);
        }

        Integer projectId = (Integer) session.getAttributes().get("projectId");
        var project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        var projectTemplate = projectTemplateRepository.findById(project.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Project Template not found"));
        try {
            Process process = codeExecutorFactory.getExecutor(ProjectTemplate.fromValue(project.getTemplateId()))
                    .execute(project.getProjectPath(),projectTemplate.getTemplatePathScript());

            setupProcess(session, process);
        } catch (IOException e) {
            broadcaster.sendMessage(session, new Message<>("error-console-text",
                    "Failed to start process: " + e.getMessage()));
        }
    }

    private void setupProcess(WebSocketSession session, Process process) {
        BufferedWriter processInputWriter = new BufferedWriter(
                new OutputStreamWriter(process.getOutputStream()));
        processMap.put(session, process);
        writerMap.put(session, processInputWriter);

        executorService.submit(() -> handleProcessOutput(session, process.getInputStream(), "output-console-text"));
        executorService.submit(() -> handleProcessOutput(session, process.getErrorStream(), "error-console-text"));
        executorService.submit(() -> monitorProcess(session, process));
    }

    private void handleInput(WebSocketSession session, Message<?> message) throws IOException {
        Message<String> inputMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                String.class
        );

        BufferedWriter writer = writerMap.get(session);
        if (writer != null) {
            try {
                writer.write(inputMessage.getData() + "\n");
                writer.flush();
            } catch (IOException e) {
                broadcaster.sendMessage(session, new Message<>("error-console-text",
                        "Unable to input: " + e.getMessage()));
            }
        } else {
            broadcaster.sendMessage(session, new Message<>("error-console-text",
                    "No process running for this session"));
        }
    }

    private void handleProcessOutput(WebSocketSession session, InputStream inputStream, String messageType) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                broadcaster.sendMessage(session, new Message<>(messageType, line));
            }
        } catch (IOException e) {
            broadcaster.sendMessage(session, new Message<>("error-console-text",
                    "Failed to read process output: " + e.getMessage()));
        }
    }

    private void monitorProcess(WebSocketSession session, Process process) {
        try {
            int exitCode = process.waitFor();
            broadcaster.sendMessage(session, new Message<>("finish-process",
                    "Process finished with exit code: " + exitCode));
        } catch (InterruptedException e) {
            broadcaster.sendMessage(session, new Message<>("error-console-text",
                    "Process execution was interrupted: " + e.getMessage()));
        } finally {
            cleanup(session);
        }
    }

    public void cleanup(WebSocketSession session) {
        Process process = processMap.remove(session);
        BufferedWriter writer = writerMap.remove(session);

        if (process != null) {
            process.destroy();
        }
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {

            }
        }
    }


}