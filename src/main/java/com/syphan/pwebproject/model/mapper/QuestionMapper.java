package com.syphan.pwebproject.model.mapper;

import com.syphan.pwebproject.model.dto.QuestionDto;
import com.syphan.pwebproject.model.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    public static final QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    public abstract Question dtoToEntity(QuestionDto questionDto);

    public abstract QuestionDto entityToDto(Question questionEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    public abstract Question updateEntity(Question questionEntityOrig, @MappingTarget Question questionEntityDest);
}
