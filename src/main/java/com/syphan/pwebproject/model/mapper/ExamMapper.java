package com.syphan.pwebproject.model.mapper;

import com.syphan.pwebproject.model.dto.ExamDto;
import com.syphan.pwebproject.model.entity.ExamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    public static final ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);

    public abstract ExamEntity dtoToEntity(ExamDto examDto);

    public abstract ExamDto entityToDto(ExamEntity examEntity);

    @Mapping(target = "id", ignore = true)
    public abstract ExamEntity updateEntity(ExamEntity examEntityOrig, @MappingTarget ExamEntity examEntityDest);
}
