package org.test.editor.util.constant;

public enum ProjectCommand {
    GET_PROJECT_STRUCTURE("get-project-structure");


    private final String action;

    ProjectCommand(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
