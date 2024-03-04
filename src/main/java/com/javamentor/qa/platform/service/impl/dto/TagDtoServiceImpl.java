package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.models.dto.RelatedTagDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.service.abstracts.dto.TagDtoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagDtoServiceImpl implements TagDtoService {
    private final TagDtoDao tagDtoDao;

    public TagDtoServiceImpl(TagDtoDao tagDtoDao) {
        this.tagDtoDao = tagDtoDao;
    }

    @Override
    public List<TagDto> getTop3TagsByUserId(Long userId) {
        return tagDtoDao.getTop3TagsByUserId(userId);
    }

    @Override
    public Optional<TagDto> getTag(Long tagId) {
        return tagDtoDao.getTag(tagId);
    }

    @Override
    public Optional<List<RelatedTagDto>> getTop10Tags() {
        List<RelatedTagDto> topTags = tagDtoDao.getTopTags();
        return topTags.isEmpty() ? Optional.empty() : Optional.of(topTags);
    }
}
