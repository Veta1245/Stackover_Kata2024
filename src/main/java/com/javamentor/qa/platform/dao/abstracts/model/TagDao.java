package com.javamentor.qa.platform.dao.abstracts.model;

import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.Optional;

public interface TagDao extends ReadWriteDao<Tag, Long> {

    Optional<Tag> getTagByName(String name);
}
