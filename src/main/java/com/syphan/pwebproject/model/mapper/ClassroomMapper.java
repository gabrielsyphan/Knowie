package com.syphan.pwebproject.model.mapper;

import com.syphan.pwebproject.model.dto.ClassroomDto;
import com.syphan.pwebproject.model.entity.Classroom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassroomMapper {

    public static final ClassroomMapper INSTANCE = Mappers.getMapper(ClassroomMapper.class);

    public abstract Classroom dtoToEntity(ClassroomDto classroomDto);

    public abstract ClassroomDto entityToDto(Classroom classroomEntity);

    @Mapping(target = "id", ignore = true)
    public abstract Classroom updateEntity(Classroom classroomEntityOrig, @MappingTarget Classroom classroomEntityDest);
}
