package org.test.editor.core.dto;

public record UserDTO (
    Integer userId,
    String email,
    String name,
    Integer roleId)
{}
