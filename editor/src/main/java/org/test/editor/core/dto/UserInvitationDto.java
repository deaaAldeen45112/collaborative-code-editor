package org.test.editor.core.dto;


public record UserInvitationDto (
     Integer userId
    , String name
    , String email)
    {}
