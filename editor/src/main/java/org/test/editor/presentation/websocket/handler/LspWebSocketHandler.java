package org.test.editor.presentation.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.test.editor.presentation.websocket.WebSocketCommandRegistry;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LspWebSocketHandler extends TextWebSocketHandler {
    private final MessageParser messageParser;
    private final WebSocketCommandRegistry commandRegistry;
    private final WebSocketSessionManager sessionManager;
    private final LspCommandHandler lspCommandHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

            Message<?> initMessage = new Message<>("lsp.initialize", null);
            lspCommandHandler.handle(session, initMessage);

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        handleMessage(session, message.getPayload());
    }
    private void handleMessage(WebSocketSession session, String jsonMessage) {
        try {
            Message<?> message = messageParser.parseMessage(jsonMessage);
            if (message.getAction() != null && message.getAction().startsWith("lsp.")) {
                lspCommandHandler.handle(session, message);
            } else {
                BaseCommandHandler handler = commandRegistry.getHandler(message.getAction());
                if (handler != null) {
                    handler.handle(session, message);
                } else {
                    System.out.println("Unknown action: " + message.getAction());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            Integer projectId = (Integer) session.getAttributes().get("projectId");
            if (projectId != null) {
                sessionManager.removeProjectSession(projectId, session);
            }
            Message<?> shutdownMessage = new Message<>("lsp.shutdown", null);
            lspCommandHandler.handle(session, shutdownMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}