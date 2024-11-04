package org.test.editor.presentation.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.test.editor.core.dto.CreateFolderDTO;
import org.test.editor.core.dto.CreatePrimaryFolderDTO;
import org.test.editor.core.service.FolderService;
import org.test.editor.core.service.ProjectService;
import org.test.editor.presentation.websocket.WebSocketMessageBroadcaster;
import org.test.editor.presentation.websocket.WebSocketSessionManager;
import org.test.editor.util.Message;
import org.test.editor.util.MessageParser;
import org.test.editor.util.constant.FolderCommand;

import java.io.IOException;

import static org.test.editor.util.constant.ProjectCommand.GET_PROJECT_STRUCTURE;

@Component
public class FolderCommandHandler extends BaseCommandHandler {
    private final FolderService folderService;
    private final ProjectService projectService;


    public FolderCommandHandler(
            WebSocketMessageBroadcaster broadcaster,
            MessageParser messageParser,
            FolderService folderService,
            ProjectService projectService
            ) {
        super(broadcaster, messageParser);
        this.folderService = folderService;
        this.projectService = projectService;

    }
    @Override
    public boolean canHandle(String action) {
        return FolderCommand.fromString(action) != null;
    }

    @Override
    public void handle(WebSocketSession session, Message<?> message) throws IOException {
        Integer projectId = (Integer) session.getAttributes().get("projectId");

        FolderCommand command = FolderCommand.fromString(message.getAction());
        if (command == null) {
            throw new IllegalArgumentException("Unknown folder command: " + message.getAction());
        }

        switch (command) {
            case CREATE_FOLDER -> handleCreateFolder(message, projectId);
            case CREATE_PRIMARY_FOLDER -> handleCreatePrimaryFolder(message, projectId);
            case DELETE_FOLDER -> handleDeleteFolder(message, projectId);
            case PROJECT_STRUCTURE -> handleProjectStructure(session, projectId);
        }
    }

    private void handleCreateFolder(Message<?> message, Integer projectId)
            throws IOException {
        Message<CreateFolderDTO> createFolderMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                CreateFolderDTO.class
        );
        var folder = folderService.createFolder(createFolderMessage.getData());
        broadcaster.broadcastMessageToProjectMembers(projectId, new Message(GET_PROJECT_STRUCTURE.getAction(), projectService.getFolderTree(projectId)));

    }

    private void handleCreatePrimaryFolder(Message<?> message, Integer projectId)
            throws IOException {
        Message<CreatePrimaryFolderDTO> createPrimaryFolderDTOMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                CreatePrimaryFolderDTO.class
        );
        var primaryFolder = folderService.createPrimaryFolder(createPrimaryFolderDTOMessage.getData());
        broadcaster.broadcastMessageToProjectMembers(projectId, new Message(GET_PROJECT_STRUCTURE.getAction(), projectService.getFolderTree(projectId)));

    }

    private void handleDeleteFolder(Message<?> message, Integer projectId)
            throws IOException {
        Message<Integer> deleteFolderMessage = messageParser.parseMessage(
                messageParser.parseMessage(message),
                Integer.class
        );

        folderService.delete(deleteFolderMessage.getData());
        broadcaster.broadcastMessageToProjectMembers(projectId, new Message(GET_PROJECT_STRUCTURE.getAction(), projectService.getFolderTree(projectId)));

    }

    private void handleProjectStructure(WebSocketSession session, Integer projectId) throws IOException {

    }


}