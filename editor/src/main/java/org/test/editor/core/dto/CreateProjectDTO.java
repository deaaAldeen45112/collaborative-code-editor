package org.test.editor.core.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProjectDTO (
        @NotBlank(message = "Project Name is required") String projectName,
        @NotNull(message = "Template Id is required") Integer templateId,
        @NotBlank(message = "Description is required") String description)
{
}
