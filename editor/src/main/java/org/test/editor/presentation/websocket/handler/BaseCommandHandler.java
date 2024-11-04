package org.test.editor.presentation.websocket.handler;

import org.springframework.web.socket.WebSocketSession;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;

import java.io.IOException;

public abstract class BaseCommandHandler {
    protected final WebSocketMessageBroadcaster broadcaster;
    protected final MessageParser messageParser;

    protected BaseCommandHandler(WebSocketMessageBroadcaster broadcaster, MessageParser messageParser) {
        this.broadcaster = broadcaster;
        this.messageParser = messageParser;
    }

    public abstract boolean canHandle(String action);
    public abstract void handle(WebSocketSession session, Message<?> message) throws IOException;
}