package org.test.editor.core.mapper;

import org.mapstruct.Mapper;
import org.test.editor.core.dto.RegisterDTO;
import org.test.editor.core.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterDTO registerDTO);
}