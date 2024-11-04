package org.test.editor.presentation.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EditorOperationsWebSocketHandler extends TextWebSocketHandler {
    private final MessageParser messageParser;
    private final WebSocketCommandRegistry commandRegistry;
    private final WebSocketSessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(EditorOperationsWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get("sub");
        Integer projectId = (Integer) session.getAttributes().get("projectId");
        if (username != null && projectId != null) {
            sessionManager.addProjectSession(projectId, session);
            System.out.println("Session added for project ID: " + projectId + " with user: " + username);
        } else {
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        handleMessage(session, message.getPayload());
    }

    private void handleMessage(WebSocketSession session, String jsonMessage) {
        try {
            Message<?> message = messageParser.parseMessage(jsonMessage);
            BaseCommandHandler handler = commandRegistry.getHandler(message.getAction());

            if (handler != null) {
                handler.handle(session, message);
            } else {
                System.out.println("Unknown action: " + message.getAction());
            }
        } catch (IOException e) {
            logger.error("IO Exception: {}", e.getMessage());
        }
        catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("finish my connection");
        Integer projectId = (Integer) session.getAttributes().get("projectId");
        if (projectId != null) {
            sessionManager.removeProjectSession(projectId, session);
        }
        sessionManager.removeSessionFromAllDiscussions(session);

    }
}
