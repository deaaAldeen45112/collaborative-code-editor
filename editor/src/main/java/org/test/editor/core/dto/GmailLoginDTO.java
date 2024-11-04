package org.test.editor.core.dto;

import jakarta.validation.constraints.NotBlank;

public class GmailLoginDTO {

    @NotBlank(message = "ID token is required.")
    private String idToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}