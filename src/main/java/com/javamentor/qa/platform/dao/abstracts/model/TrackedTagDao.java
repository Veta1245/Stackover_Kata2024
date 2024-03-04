package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;

public interface TrackedTagDao extends ReadWriteDao<TrackedTag, Long> {
    Long getTheTagCountInATagTracked(Long userId, Long tagId);
}
