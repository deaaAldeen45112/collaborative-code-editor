package org.test.editor.presentation.websocket.handler;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.core.dto.CreateFileDTO;
import org.test.editor.core.dto.ReadFileDTO;
import org.test.editor.core.dto.UpdateFileDTO;
import org.test.editor.core.service.FileService;
import org.test.editor.core.service.ProjectService;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;
import org.test.editor.util.constant.FileCommand;

import java.io.IOException;

import static org.test.editor.util.constant.FileCommand.GET_FILE_CONTENT;
import static org.test.editor.util.constant.ProjectCommand.GET_PROJECT_STRUCTURE;

@Component
public class FileCommandHandler extends BaseCommandHandler {
    private final FileService fileService;
    private final ProjectService projectService;


    public FileCommandHandler(
            WebSocketMessageBroadcaster broadcaster,
            MessageParser messageParser,
            FileService fileService,
            ProjectService projectService
           ) {
        super(broadcaster, messageParser);
        this.fileService = fileService;
        this.projectService = projectService;
    }

    @Override
    public boolean canHandle(String action) {
        return FileCommand.fromString(action) != null;
    }

    @Override
    public void handle(WebSocketSession session, Message<?> message) throws IOException {
        Integer projectId = (Integer) session.getAttributes().get("projectId");
        Integer userId = (Integer) session.getAttributes().get("userId");
        String userName= (String) session.getAttributes().get("userName");
        FileCommand command = FileCommand.fromString(message.getAction());
        if (command == null) {
            throw new IllegalArgumentException("Unknown file command: " + message.getAction());
        }

        switch (command) {
            case CREATE_FILE -> handleCreateFile(message, projectId, userId);
            case DELETE_FILE -> handleDeleteFile(message, projectId);
            case EDIT_FILE -> handleEditFile(session, message, projectId, userId);
        }
    }
    private void handleCreateFile(Message<?> message, Integer projectId, Integer userId) throws IOException {
        Message<CreateFileDTO> createFileMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                CreateFileDTO.class
        );

        fileService.createFileByUser(createFileMessage.getData(), userId);
        broadcaster.broadcastMessageToProjectMembers(
                projectId,
                new Message<>(GET_PROJECT_STRUCTURE.getAction(), projectService.getFolderTree(projectId))
        );
    }

    private void handleDeleteFile(Message<?> message, Integer projectId) throws IOException {
        Message<Integer> deleteFileMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                Integer.class
        );

        fileService.deleteFile(deleteFileMessage.getData());
        broadcaster.broadcastMessageToProjectMembers(
                projectId,
                new Message<>(GET_PROJECT_STRUCTURE.getAction(), projectService.getFolderTree(projectId))
        );
    }

    private void handleEditFile(WebSocketSession session, Message<?> message, Integer projectId, Integer userId) throws IOException {
        Message<UpdateFileDTO> updateFileMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                UpdateFileDTO.class
        );

        fileService.editFileByUser(updateFileMessage.getData(), userId);
        var readFileDTO = new ReadFileDTO(
                fileService.getFileById(updateFileMessage.getData().fileId()),
                updateFileMessage.getData().fileId()
        );

        broadcaster.broadcastMessageToProjectMembersExceptSender(
                session,
                projectId,
                new Message<>(GET_FILE_CONTENT.getAction(), readFileDTO)
        );
    }

}