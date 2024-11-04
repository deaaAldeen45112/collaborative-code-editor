package org.test.editor.presentation.config;

import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.test.editor.presentation.websocket.handler.CodeRunWebSocketHandler;
import org.test.editor.presentation.websocket.handler.EditorOperationsWebSocketHandler;
import org.test.editor.presentation.websocket.handler.LspWebSocketHandler;
import org.test.editor.presentation.websocket.interceptor.WebSocketJwtAuthInterceptor;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketJwtAuthInterceptor webSocketJwtAuthInterceptor;
    private final CodeRunWebSocketHandler codeEditorWebSocketHandler;
    private final EditorOperationsWebSocketHandler editorOperationsWebSocketHandler;
    private final LspWebSocketHandler lspWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(editorOperationsWebSocketHandler, "/editor-operations")
                .addInterceptors(webSocketJwtAuthInterceptor)
                .setAllowedOrigins("*");

        registry.addHandler(codeEditorWebSocketHandler, "/code-run")
                .addInterceptors(webSocketJwtAuthInterceptor)
                .setAllowedOrigins("*");
        registry.addHandler(lspWebSocketHandler, "/lsp")
              //  .addInterceptors(webSocketJwtAuthInterceptor)
                .setAllowedOrigins("*");

    }


}