package org.test.editor.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private String status;
    private T data;
    private List<String> validationErrors;

    public ApiResponse(String message, String status, T data) {
      this.message = message;
      this.status = status;
      this.data=data;
    }

    public ApiResponse(T data) {
        this.data=data;
    }
    public ApiResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public ApiResponse(String message, String status, List<String> errors) {
        this.message = message;
        this.status = status;
        this.validationErrors=errors;
    }
}
