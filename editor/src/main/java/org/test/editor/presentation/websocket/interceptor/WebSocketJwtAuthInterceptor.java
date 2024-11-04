package org.test.editor.presentation.websocket.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.test.editor.infra.repository.CollaboratorRepository;
import org.test.editor.infra.service.JwtService;

import java.util.Map;

@Component
public class WebSocketJwtAuthInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final CollaboratorRepository collaboratorRepository;

    public WebSocketJwtAuthInterceptor(JwtService jwtService, CollaboratorRepository collaboratorRepository) {
        this.jwtService = jwtService;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = servletRequest.getParameter("token");
            String projectId = servletRequest.getParameter("projectId");
            if (projectId!=null && token != null && jwtService.isTokenValid(token)) {
                String sub = jwtService.extractSubject(token);
                var userId=jwtService.extractUserId(token);
                var claims=jwtService.extractAllClaims(token);
                var userName=(String) claims.get("userName");
                var hasProject=collaboratorRepository.existsByEmailAndProjectId(sub,Integer.parseInt(projectId));;
                if (!hasProject) {
                    return false;
                }
                attributes.put("userId", userId);
                attributes.put("sub", sub);
                attributes.put("projectId", Integer.valueOf(projectId));
                attributes.put("userName", userName);
                return true;
            }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception ex) {

    }
}
