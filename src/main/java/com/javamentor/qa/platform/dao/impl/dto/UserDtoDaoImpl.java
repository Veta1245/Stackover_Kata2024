package com.javamentor.qa.platform.dao.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.UserDtoDao;
import com.javamentor.qa.platform.dao.util.SingleResultUtil;
import com.javamentor.qa.platform.models.dto.UserDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserDtoDaoImpl implements UserDtoDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserDto> getById(Long userId) {

        return SingleResultUtil.getSingleResultOrNull(entityManager.createQuery(
                        """
                                    SELECT distinct new com.javamentor.qa.platform.models.dto.UserDto(
                                        u.id,
                                        u.email,
                                        u.fullName,
                                        u.imageLink,
                                        u.city,
                                        cast(sum(r.count) as long),
                                        u.persistDateTime,
                                        cast(
                                             (
                                                 (SELECT count (va) as vac  FROM VoteAnswer va WHERE va.user = u) +
                                                 (SELECT count (vq) as vqc  FROM VoteQuestion vq WHERE vq.user = u)
                                             )
                                        as long)
                                    )
                                    FROM User u
                                    left join Reputation r ON r.author = u 
                                    WHERE u.id = :id
                                    GROUP BY u.id
                                """,
                        UserDto.class)
                .setParameter("id", userId));
    }
}