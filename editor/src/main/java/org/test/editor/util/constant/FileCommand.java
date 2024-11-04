package org.test.editor.util.constant;

public enum FileCommand {
    CREATE_FILE("create-file"),
    DELETE_FILE("delete-file"),
    EDIT_FILE("edit-file"),
    GET_FILE_CONTENT("get-file-content");

    private final String action;

    FileCommand(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static FileCommand fromString(String text) {
        for (FileCommand command : FileCommand.values()) {
            if (command.getAction().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }
}
