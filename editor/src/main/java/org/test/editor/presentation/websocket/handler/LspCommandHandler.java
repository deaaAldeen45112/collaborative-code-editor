package org.test.editor.presentation.websocket.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class LspCommandHandler extends BaseCommandHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ConcurrentHashMap<WebSocketSession, Process> lspProcesses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, BufferedWriter> lspWriters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, AtomicInteger> requestIds = new ConcurrentHashMap<>();

    public LspCommandHandler(WebSocketMessageBroadcaster broadcaster,
                             MessageParser messageParser) {
        super(broadcaster, messageParser);
    }

    @Override
    public boolean canHandle(String action) {
        return action.startsWith("lsp.");
    }

    @Override
    public void handle(WebSocketSession session, Message<?> message) {
        String action = message.getAction();
        switch (action) {
            case "lsp.initialize":
                initializeLspServer(session);
                break;
            case "lsp.shutdown":
                shutdownLspServer(session);
                break;
            case "lsp.didOpen":
            case "lsp.didChange":
            case "lsp.completion":
                handleLspRequest(session, message);
                break;
        }
    }

    private void initializeLspServer(WebSocketSession session) {
        if (lspProcesses.containsKey(session)) {
            return;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("clangd");
            Process process = processBuilder.start();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)
            );
            lspProcesses.put(session, process);
            lspWriters.put(session, writer);
            requestIds.put(session, new AtomicInteger(1));
            executorService.submit(() -> handleLspOutput(session, process));
            executorService.submit(() -> handleLspError(session, process));
            sendInitializeRequest(session);

        } catch (IOException e) {
            broadcaster.sendMessage(session,
                    new Message<>("lsp.error", "Failed to start LSP server: " + e.getMessage()));
        }
    }

    private void handleLspError(WebSocketSession session, Process process) {
        try (BufferedReader errorReader =new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    broadcaster.sendMessage(session, new Message<>("lsp.response", line));
                    System.err.println("Error from server: " + line);
                }
            } catch (IOException e) {
            System.err.println("Error reading from error stream: " + e.getMessage());
            e.printStackTrace();

        }
    }


    private void handleLspOutput(WebSocketSession session, Process process) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                broadcaster.sendMessage(session, new Message<>("lsp.response", line));
                System.out.println("Message from server: " + line);
            }
        } catch (IOException e) {
            broadcaster.sendMessage(session,
                    new Message<>("lsp.error", "Error reading LSP server output: " + e.getMessage()));
        }
    }

    private void handleLspRequest(WebSocketSession session, Message<?> message) {
        BufferedWriter writer = lspWriters.get(session);
        if (writer == null) {
            broadcaster.sendMessage(session,
                    new Message<>("lsp.error", "LSP server not initialized"));
            return;
        }

        try {
            JsonNode lspRequest = createLspRequest(session,message);
            String request = createLspMessage(lspRequest.toString());

            writer.write(request);
            writer.flush();
        } catch (IOException e) {
            broadcaster.sendMessage(session,
                    new Message<>("lsp.error", "Failed to send LSP request: " + e.getMessage()));
        }
    }

    private JsonNode createLspRequest(WebSocketSession session,Message<?> message) throws IOException {
        int id = requestIds.get(session).getAndIncrement();
        switch (message.getAction()) {
            case "lsp.didOpen":
                return createDidOpenRequest(message.getData(), id);
            case "lsp.didChange":
                return createDidChangeRequest(message.getData(), id);
            case "lsp.completion":
                return createCompletionRequest(message.getData(), id);

            default:
                throw new IllegalArgumentException("Unsupported LSP action: " + message.getAction());
        }
    }

    private String createLspMessage(String content) {
        return String.format("Content-Length: %d\r\n\r\n%s",
                content.getBytes(StandardCharsets.UTF_8).length, content);
    }

    private void sendInitializeRequest(WebSocketSession session) throws IOException {
        String initializeRequest = createInitializeRequest();
        BufferedWriter writer = lspWriters.get(session);
        writer.write(createLspMessage(initializeRequest));
        writer.flush();
    }

    private String createInitializeRequest() {
        return "{\n" +
                "    \"jsonrpc\": \"2.0\",\n" +
                "    \"id\": 0,\n" +
                "    \"method\": \"initialize\",\n" +
                "    \"params\": {\n" +
                "        \"processId\": null,\n" +
                "        \"clientInfo\": {\n" +
                "            \"name\": \"Test Editor\",\n" +
                "            \"version\": \"1.0\"\n" +
                "        },\n" +
                "        \"capabilities\": {\n" +
                "            \"textDocument\": {\n" +
                "                \"synchronization\": {\n" +
                "                    \"dynamicRegistration\": true,\n" +
                "                    \"willSave\": true,\n" +
                "                    \"willSaveWaitUntil\": true,\n" +
                "                    \"didSave\": true\n" +
                "                },\n" +
                "                \"completion\": {\n" +
                "                    \"dynamicRegistration\": true,\n" +
                "                    \"completionItem\": {\n" +
                "                        \"snippetSupport\": true\n" +
                "                    }\n" +
                "                },\n" +
                "                \"diagnostic\": {\n" +
                "                    \"dynamicRegistration\": true\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    private void shutdownLspServer(WebSocketSession session) {
        Process process = lspProcesses.remove(session);
        BufferedWriter writer = lspWriters.remove(session);
        requestIds.remove(session);

        if (process != null) {
            try {
                if (writer != null) {
                    writer.write(createLspMessage(
                            "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"shutdown\"}"
                    ));
                    writer.flush();
                    writer.close();
                }
                process.destroy();
            } catch (IOException e) {
                broadcaster.sendMessage(session,
                        new Message<>("lsp.error", "Error during LSP server shutdown: " + e.getMessage()));
            }
        }
    }

    private int parseContentLength(String headers) {
        for (String header : headers.split("\r\n")) {
            if (header.toLowerCase().startsWith("content-length:")) {
                return Integer.parseInt(header.substring("content-length:".length()).trim());
            }
        }
        return 0;
    }

    private JsonNode createDidOpenRequest(Object data, int id) throws IOException {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("jsonrpc", "2.0");
        request.put("method", "textDocument/didOpen");
        JsonNode paramsData = objectMapper.valueToTree(data);
        ObjectNode params = request.putObject("params");
        ObjectNode textDocument = params.putObject("textDocument");
        textDocument.put("uri", paramsData.get("textDocument").get("uri").asText());
        textDocument.put("languageId", paramsData.get("textDocument").get("languageId").asText());
        textDocument.put("version", paramsData.get("textDocument").get("version").asInt());
        textDocument.put("text", paramsData.get("textDocument").get("text").asText());

        return request;
    }
    private JsonNode createDidChangeRequest(Object data, int id) throws IOException {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("jsonrpc", "2.0");
        request.put("method", "textDocument/didChange");
        request.put("id", id);

        // Convert input data to JsonNode for easier processing
        JsonNode paramsData = objectMapper.valueToTree(data);

        // Create params object
        ObjectNode params = request.putObject("params");

        // Handle textDocument
        ObjectNode textDocument = params.putObject("textDocument");
        textDocument.put("uri", paramsData.get("textDocument").get("uri").asText());
        textDocument.put("version", paramsData.get("textDocument").get("version").asInt());

        // Handle contentChanges array
        ArrayNode contentChanges = params.putArray("contentChanges");
        JsonNode inputChanges = paramsData.get("contentChanges");

        // Process each content change
        if (inputChanges.isArray()) {
            for (JsonNode change : inputChanges) {
                ObjectNode changeObj = contentChanges.addObject();
                if (change.has("text")) {
                    changeObj.put("text", change.get("text").asText());
                }
                // Optionally handle range if present
                if (change.has("range")) {
                    JsonNode range = change.get("range");
                    ObjectNode rangeObj = changeObj.putObject("range");

                    ObjectNode start = rangeObj.putObject("start");
                    start.put("line", range.get("start").get("line").asInt());
                    start.put("character", range.get("start").get("character").asInt());

                    ObjectNode end = rangeObj.putObject("end");
                    end.put("line", range.get("end").get("line").asInt());
                    end.put("character", range.get("end").get("character").asInt());
                }
            }
        }

        return request;
    }
    public  JsonNode createCompletionRequest(Object data, int id) throws IOException {
        ObjectNode request = objectMapper.createObjectNode();
        request.put("jsonrpc", "2.0");
        request.put("id", id);
        request.put("method", "textDocument/completion");
        JsonNode paramsData = objectMapper.valueToTree(data);
        ObjectNode params = request.putObject("params");
        params.putObject("textDocument").put("uri", paramsData.get("textDocument").get("uri").asText());
        params.putObject("position").put("line", paramsData.get("line").get("uri").asText()).put("character", paramsData.get("character").get("uri").asText());

       return request;
    }
}