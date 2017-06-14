package com.xinra.growthlectures.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface AbstractLectureRepository<T extends Lecture> 
    extends AbstractNamedEntityRepository<T> {

  @Query("SELECT l.name FROM Lecture l WHERE l.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
}
