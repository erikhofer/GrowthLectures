package com.xinra.growthlectures.entity

import com.xinra.nucleus.entity.AbstractEntityRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository

@Repository
public interface MediaRepository extends AbstractMediaRepository<Media> {}

@NoRepositoryBean
public interface AbstractMediaRepository<T extends Media>
    extends AbstractEntityRepository<T> {
       
}

