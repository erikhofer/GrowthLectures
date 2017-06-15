package com.xinra.growthlectures.entity;

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends AbstractLectureRepository<Lecture> {}

@NoRepositoryBean
public interface AbstractLectureRepository<T extends Lecture>
    extends AbstractNamedEntityRepository<T> {
  
  @Query("SELECT l.id, l.category.slug FROM Lecture l WHERE l.slug = :slug")
  String[][] getIdAndCatgorySlug(@Param("slug") String slug);
  
  Lecture findBySlugAndCategorySlug(String slug, String categorySlug);

  Iterable<Lecture> findByCategorySlug(String categorySlug);
  Iterable<Lecture> findByLecturerSlug(String lecturerSlug);
  
  @Query("SELECT l.name FROM Lecture l WHERE l.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
}