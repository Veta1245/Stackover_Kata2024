package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.converters.TagConverter;
import com.javamentor.qa.platform.dao.abstracts.model.TrackedTagDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.models.entity.question.TrackedTag;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.TrackedTagService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@Service
public class TrackedTagServiceImpl extends ReadWriteServiceImpl<TrackedTag, Long> implements TrackedTagService {

    private final TrackedTagDao trackedTagDao;
    private final EntityManager entityManager;
    private final TagConverter tagConverter;

    public TrackedTagServiceImpl(ReadWriteDao<TrackedTag, Long> readWriteDao, TrackedTagDao trackedTagDao, EntityManager entityManager, TagConverter tagConverter) {
        super(readWriteDao);
        this.trackedTagDao = trackedTagDao;
        this.entityManager = entityManager;
        this.tagConverter = tagConverter;
    }

    @Transactional
    @Override
    public TagDto addTagToTracked(User user, Long tagId) {
        Long count = trackedTagDao.getTheTagCountInATagTracked(user.getId(), tagId);
        if (count == 0) {
            Tag tag = entityManager.find(Tag.class, tagId);
            TrackedTag trackedTag = new TrackedTag();
            trackedTag.setUser(user);
            trackedTag.setTrackedTag(tag);
            trackedTag.setPersistDateTime(LocalDateTime.now());
            entityManager.persist(trackedTag);
            return tagConverter.tagToTagDto(trackedTag.getTrackedTag());
        } else {
            throw new IllegalArgumentException("Тэг уже отслеживается пользователем");
        }
    }
}