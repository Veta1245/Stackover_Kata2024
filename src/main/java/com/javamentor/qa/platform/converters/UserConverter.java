package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
abstract public class UserConverter {
    @Mapping(target = "linkImage", source = "imageLink")
    @Mapping(target = "registrationDate", source = "persistDateTime")
    public abstract UserDto userToUserDto(User user);
}
