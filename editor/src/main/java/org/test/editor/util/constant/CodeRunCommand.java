package org.test.editor.util.constant;


public enum CodeRunCommand {
    RUN("run"),
    INPUT("input-console-text");

    private final String value;

    CodeRunCommand(String value) {
        this.value = value;
    }

    public static CodeRunCommand fromString(String text) {
        for (CodeRunCommand command : CodeRunCommand.values()) {
            if (command.value.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }
}