package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.UserDto;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
abstract public class UserConverter {
    @Mapping(target = "linkImage", source = "imageLink")
    @Mapping(target = "registrationDate", source = "persistDateTime")
    public abstract UserDto userToUserDto(User user);

    @Mapping( target = "fullName", expression = "java(userRegistrationDto.getFirstName() + \" \" + userRegistrationDto.getLastName())")
    public abstract User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    @Mapping( target = "firstName", source = ".", qualifiedByName = "getFirstName")
    @Mapping( target = "lastName", source = ".", qualifiedByName = "getLastName")
    public abstract UserRegistrationDto userToUserRegistrationDto(User user);

    @Named("getFirstName")
    String getFirstName(User user) {
        return user.getFullName().split(" ")[0];
    }

    @Named("getLastName")
    String getLastName(User user) {
        return user.getFullName().contains(" ")?user.getFullName().split(" ")[1]:"";
    }
}
