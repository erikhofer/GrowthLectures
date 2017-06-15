package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeMediaRepository extends AbstractYoutubeMediaRepository<YoutubeMedia> {}

@NoRepositoryBean
public interface AbstractYoutubeMediaRepository<T extends YoutubeMedia> extends AbstractEntityRepository<T> {

}