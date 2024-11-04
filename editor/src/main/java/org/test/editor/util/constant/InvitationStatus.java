package org.test.editor.util.constant;

public enum InvitationStatus {
    PENDING(1),
    ACCEPTED(2),
    REJECTED(3);

    private final int value;
    InvitationStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static InvitationStatus fromValue(int value) {
        for (InvitationStatus role : InvitationStatus.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
