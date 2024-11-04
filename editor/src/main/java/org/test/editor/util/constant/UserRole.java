package org.test.editor.util.constant;

public enum UserRole {
    ROLE_ADMIN(1),
    ROLE_CODER(2);

    private final int value;
    UserRole(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static UserRole fromValue(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
