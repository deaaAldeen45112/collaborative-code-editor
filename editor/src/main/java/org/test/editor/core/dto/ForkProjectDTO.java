package org.test.editor.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ForkProjectDTO (
        @NotNull(message = "Project Id is required") Integer projectId,
        @NotBlank(message = "Project Name is required") String projectName,
        @NotBlank(message = "Description is required") String description)
{
}
