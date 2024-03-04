package com.javamentor.qa.platform.converters;

import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class TagConverter {
    @Mappings(value = {
            @Mapping(target = "id", source = "tag.id"),
            @Mapping(target = "name", source = "tag.name"),
            @Mapping(target = "description", source = "tag.description")
            })

    public abstract TagDto tagToTagDto(Tag tag);
}
