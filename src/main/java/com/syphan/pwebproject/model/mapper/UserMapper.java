package com.syphan.pwebproject.model.mapper;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserEntity dtoToEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    public abstract UserDto entityToDto(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "firstAccess", ignore = true)
    public abstract UserEntity updateEntity(UserEntity userEntityOrig, @MappingTarget UserEntity userEntityDest);
}
