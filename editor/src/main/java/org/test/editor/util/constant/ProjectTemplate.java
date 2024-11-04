package org.test.editor.util.constant;

public enum ProjectTemplate {
    C_PLUS(1),
    PYTHON(2);

    private final int value;
    ProjectTemplate(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static ProjectTemplate fromValue(int value) {
        for (ProjectTemplate role : ProjectTemplate.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
