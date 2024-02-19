package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.entity.question.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class QuestionConverter {

    @Mappings(value = {
            @Mapping(target = "authorId", source = "user.id"),
            @Mapping(target = "authorName", source = "user.fullName"),
            @Mapping(target = "listTagDto", source = "tags"),
            @Mapping(target = "authorImage", source = "user.imageLink")
            })
    public abstract QuestionDto questionToQuestionDto(Question question);
}
