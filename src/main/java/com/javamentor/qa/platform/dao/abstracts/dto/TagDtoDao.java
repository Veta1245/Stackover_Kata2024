package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;

import java.util.List;
import java.util.Optional;

public interface TagDtoDao {
    List<TagDto> getTop3TagsByUserId(Long userId);
    Optional<TagDto> getTag(Long tagId);

    List<RelatedTagDto> getTopTags() ;
}
