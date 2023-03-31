package com.syphan.pwebproject.model.mapper;

import com.syphan.pwebproject.model.dto.UserDto;
import com.syphan.pwebproject.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    public abstract UserEntity dtoToEntity(UserDto benefitDto);

    public abstract UserDto entityToDto(UserEntity benefitEntity);
}
