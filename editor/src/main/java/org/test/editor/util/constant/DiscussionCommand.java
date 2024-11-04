package org.test.editor.util.constant;

public enum DiscussionCommand {
    CREATE_DISCUSSION("create-discussion"),
    CREATE_COMMENT("create-comment"),
    JOIN_DISCUSSION("join-discussion"),
    EXIT_DISCUSSION("exit-discussion"),
    JOIN_DISCUSSION_DONE("join-discussion-done"),
    EXIT_DISCUSSION_DONE("exit-discussion-done"),
    GET_COMMENT("get-comment"),
    GET_DISCUSSION("get-discussion");

    private final String action;

    DiscussionCommand(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static DiscussionCommand fromString(String text) {
        for (DiscussionCommand command : DiscussionCommand.values()) {
            if (command.getAction().equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }
}
