package com.xinra.growthlectures.entity;

import java.util.List
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends AbstractLecturerRepository<Lecturer> {}

@NoRepositoryBean
public interface AbstractLecturerRepository<T extends Lecturer>
    extends AbstractNamedEntityRepository<T> {
  
  @Query("SELECT l.name FROM Lecturer l WHERE l.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
  List<Lecturer> findByOrderByName(Pageable pageable);
  
  Lecturer findOneBySlug(String slug);
}
