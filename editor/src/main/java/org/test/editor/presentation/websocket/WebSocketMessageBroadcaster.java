package org.test.editor.presentation.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class WebSocketMessageBroadcaster {
    private final MessageParser messageParser;
    private final WebSocketSessionManager sessionManager;

    public void broadcastMessageToProjectMembers(Integer projectId, Message<?> messageContent) {
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionManager.getProjectSessions(projectId);
        broadcastMessageToMembers(sessions, messageContent);
    }

    public void broadcastMessageToProjectMembersExceptSender(WebSocketSession senderSession, Integer projectId, Message<?> messageContent) {
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionManager.getProjectSessions(projectId);
        broadcastMessageExceptSender(sessions, senderSession, messageContent);
    }

    public void broadcastMessageToDiscussionMembers(Integer discussionId, Message<?> messageContent) {
        ConcurrentLinkedQueue<WebSocketSession> sessions = sessionManager.getDiscussionSessions(discussionId);
        broadcastMessageToMembers(sessions, messageContent);
    }

    private void broadcastMessageToMembers(ConcurrentLinkedQueue<WebSocketSession> sessions, Message<?> messageContent) {

        if (sessions != null) {
            try {
                TextMessage message = new TextMessage(messageParser.parseMessage(messageContent));
                for (WebSocketSession session : sessions) {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessageExceptSender(ConcurrentLinkedQueue<WebSocketSession> sessions,
                                              WebSocketSession senderSession,
                                              Message<?> messageContent) {
        if (sessions != null) {
            try {
                TextMessage message = new TextMessage(messageParser.parseMessage(messageContent));
                for (WebSocketSession session : sessions) {
                    if (session.isOpen() && !session.getId().equals(senderSession.getId())) {
                        session.sendMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(WebSocketSession session, Message message) {

        if (session.isOpen()) {
            try {
                String jsonMessage = messageParser.parseMessage(message);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
               throw new RuntimeException("error send message "+e.getMessage());
            }
        }
    }

}

