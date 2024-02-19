package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.dao.abstracts.repository.ReadWriteDao;
import com.javamentor.qa.platform.models.dto.QuestionCreateDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.question.Tag;
import com.javamentor.qa.platform.service.abstracts.model.TagService;
import com.javamentor.qa.platform.service.impl.repository.ReadWriteServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl extends ReadWriteServiceImpl<Tag, Long> implements TagService {

    private final TagDao tagDao;

    public TagServiceImpl(ReadWriteDao<Tag, Long> readWriteDao, TagDao tagDao) {
        super(readWriteDao);
        this.tagDao = tagDao;
    }

    @Override
    @Transactional
    public List<Tag> addTag(QuestionCreateDto questionCreateDto) {
        List<TagDto> tagDtoList = questionCreateDto.getTags();
        List<Tag> tagList = new ArrayList<>();

        for (TagDto tagDto : tagDtoList) {
            Optional<Tag> existTagInDB = tagDao.getTagByName(tagDto.getName());
            Tag tag;
            if (existTagInDB.isEmpty()) {
                tag = new Tag();
                tag.setName(tagDto.getName());
                tag.setDescription(tagDto.getDescription());
                tagDao.persist(tag);
            } else {
                tag = existTagInDB.get();
            }
            tagList.add(tag);
        }
        return tagList;
    }
}
