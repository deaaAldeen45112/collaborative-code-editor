package org.test.editor.core.dto;

import jakarta.validation.constraints.NotBlank;

public class GithubLoginDTO {

    @NotBlank(message = "Authorization code is required.")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
