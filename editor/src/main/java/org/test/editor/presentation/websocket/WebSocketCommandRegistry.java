package org.test.editor.presentation.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.test.editor.presentation.websocket.handler.BaseCommandHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketCommandRegistry {
    private final List<BaseCommandHandler> commandHandlers;

    public BaseCommandHandler getHandler(String action) {
        return commandHandlers.stream()
                .filter(handler -> handler.canHandle(action))
                .findFirst()
                .orElse(null);
    }
}
