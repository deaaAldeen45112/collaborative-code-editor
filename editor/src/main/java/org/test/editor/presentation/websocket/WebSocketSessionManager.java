package org.test.editor.presentation.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class WebSocketSessionManager {
    private final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<WebSocketSession>> projectSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, ConcurrentLinkedQueue<WebSocketSession>> discussionSessions = new ConcurrentHashMap<>();

    public void addProjectSession(Integer projectId, WebSocketSession session) {
        projectSessions.computeIfAbsent(projectId, k -> new ConcurrentLinkedQueue<>()).add(session);
    }

    public void addDiscussionSession(Integer discussionId, WebSocketSession session) {
        discussionSessions.computeIfAbsent(discussionId, k -> new ConcurrentLinkedQueue<>()).add(session);
    }

    public void removeProjectSession(Integer projectId, WebSocketSession session) {
        ConcurrentLinkedQueue<WebSocketSession> sessions = projectSessions.get(projectId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public void removeDiscussionSession(Integer discussionId, WebSocketSession session) {
        ConcurrentLinkedQueue<WebSocketSession> sessions = discussionSessions.get(discussionId);
        if (sessions != null) {
            sessions.remove(session);
        }
    }

    public ConcurrentLinkedQueue<WebSocketSession> getProjectSessions(Integer projectId) {
        return projectSessions.get(projectId);
    }

    public ConcurrentLinkedQueue<WebSocketSession> getDiscussionSessions(Integer discussionId) {
        return discussionSessions.get(discussionId);
    }
    public Set<Integer> getAllDiscussionIds() {
        return discussionSessions.keySet();
    }
    public void removeSessionFromAllDiscussions(WebSocketSession session){
        for (Integer discussionId : getAllDiscussionIds()) {
            removeDiscussionSession(discussionId, session);
        }
    }
}