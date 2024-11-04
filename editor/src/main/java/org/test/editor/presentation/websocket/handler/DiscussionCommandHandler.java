package org.test.editor.presentation.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.core.dto.CreateCommentDTO;
import org.test.editor.core.dto.CreateDiscussionDTO;
import org.test.editor.core.dto.JoinDiscussionDTO;
import org.test.editor.core.service.CommentService;
import org.test.editor.core.service.DiscussionService;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;
import org.test.editor.util.constant.DiscussionCommand;

import java.io.IOException;

import static org.test.editor.util.constant.DiscussionCommand.*;

@Component
public class DiscussionCommandHandler extends BaseCommandHandler {
    private final DiscussionService discussionService;
    private final CommentService commentService;
    private final WebSocketSessionManager sessionManager;

    public DiscussionCommandHandler(
            WebSocketMessageBroadcaster broadcaster,
            MessageParser messageParser,
            DiscussionService discussionService,
            CommentService commentService,
            WebSocketSessionManager sessionManager) {
        super(broadcaster, messageParser);
        this.discussionService = discussionService;
        this.commentService = commentService;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean canHandle(String action) {
        return DiscussionCommand.fromString(action) != null;
    }

    @Override
    public void handle(WebSocketSession session, Message<?> message) throws IOException {
        Integer projectId = (Integer) session.getAttributes().get("projectId");
        DiscussionCommand command = DiscussionCommand.fromString(message.getAction());
        if (command == null) {
            throw new IllegalArgumentException("Unknown discussion command: " + message.getAction());
        }

        switch (command) {
            case CREATE_DISCUSSION -> handleCreateDiscussion(message,projectId);
            case CREATE_COMMENT -> handleCreateComment(message);
            case JOIN_DISCUSSION -> handleJoinDiscussion(session, message);
            case EXIT_DISCUSSION -> handleExitDiscussion(session, message);
        }
    }

    private void handleJoinDiscussion(WebSocketSession session, Message<?> message) throws IOException {
        Message<JoinDiscussionDTO> joinDiscussionDTOMessage = messageParser.parseMessage(
                messageParser.parseMessage(message)
                , JoinDiscussionDTO.class);
        sessionManager.addDiscussionSession(joinDiscussionDTOMessage.getData().discussionId(),session);
        broadcaster.sendMessage(session, new Message(JOIN_DISCUSSION_DONE.getAction(),"join discussion done"));

    }

    private void handleExitDiscussion(WebSocketSession session, Message<?> message) throws IOException {
        Message<JoinDiscussionDTO> exitDiscussionDTOMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                JoinDiscussionDTO.class);
        sessionManager.removeDiscussionSession(exitDiscussionDTOMessage.getData().discussionId(),session);
        broadcaster.sendMessage(session, new Message(EXIT_DISCUSSION_DONE.getAction(), "exit discussion done"));

    }

    private void handleCreateComment(Message<?> message) throws IOException {
        Message<CreateCommentDTO> createCommentDTOMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                CreateCommentDTO.class);
        var savedComment= commentService.createComment(createCommentDTOMessage.getData());
        broadcaster.broadcastMessageToDiscussionMembers(createCommentDTOMessage.getData().discussionId(), new Message(GET_COMMENT.getAction(), savedComment));

    }

    private void handleCreateDiscussion(Message<?> message, Integer projectId) throws IOException {

        Message<CreateDiscussionDTO> createDiscussionDTOMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                CreateDiscussionDTO.class);
        var savedDiscussion=discussionService.createDiscussion(createDiscussionDTOMessage.getData());
        broadcaster.broadcastMessageToProjectMembers(projectId,new Message(GET_DISCUSSION.getAction(), savedDiscussion));

    }

}