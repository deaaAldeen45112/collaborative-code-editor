package org.test.editor.util.constant;

public enum CollaboratorRole {
    OWNER(1),
    MEMBER(2);

    private final int value;
    CollaboratorRole(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static CollaboratorRole fromValue(int value) {
        for (CollaboratorRole role : CollaboratorRole.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}