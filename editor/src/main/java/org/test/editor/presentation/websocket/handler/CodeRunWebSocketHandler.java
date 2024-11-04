package org.test.editor.presentation.websocket.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CodeRunWebSocketHandler extends TextWebSocketHandler {
    private final MessageParser messageParser;
    private final CodeRunCommandHandler codeRunCommandHandler;
    private final WebSocketSessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(CodeRunWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Message<?> parsedMessage = messageParser.parseMessage(message.getPayload());
            codeRunCommandHandler.handle(session, parsedMessage);
        }
        catch (IOException e) {
            try {
                session.sendMessage(new TextMessage(
                        messageParser.parseMessage(new Message<>("error-console-text",
                                "Failed to process message: " + e.getMessage()))
                ));
            } catch (IOException ex) {
                logger.error("can't parse the message: {}", e.getMessage());
            }

        }
        catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        codeRunCommandHandler.cleanup(session);
        Integer projectId = (Integer) session.getAttributes().get("projectId");
        if (projectId != null) {
            sessionManager.removeProjectSession(projectId, session);
        }
    }
}