package org.test.editor.util.constant;

public enum FolderCommand {
    CREATE_FOLDER("create-folder"),
    CREATE_PRIMARY_FOLDER("create-primary-folder"),
    DELETE_FOLDER("delete-folder"),
    PROJECT_STRUCTURE("project-structure");

    private final String action;

    FolderCommand(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static FolderCommand fromString(String text) {
        for (FolderCommand command : FolderCommand.values()) {
            if (command.getAction().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }
}