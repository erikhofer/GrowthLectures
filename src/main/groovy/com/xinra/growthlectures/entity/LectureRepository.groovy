package com.xinra.growthlectures.entity;

import groovy.transform.CompileStatic
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository;

@Repository
@CompileStatic
interface LectureRepository extends AbstractLectureRepository<Lecture> {}

@NoRepositoryBean
interface AbstractLectureRepository<T extends Lecture>
    extends AbstractNamedEntityRepository<T>, AbstractLectureRepositoryCustom<T> {
  
  @Query("SELECT l.id, l.category.slug FROM Lecture l WHERE l.slug = :slug")
  String[][] getIdAndCatgorySlug(@Param("slug") String slug);
  
  Lecture findBySlugAndCategorySlug(String slug, String categorySlug);

  
  Iterable<Lecture> findByCategorySlug(String categorySlug);
  Iterable<Lecture> findByLecturerSlug(String lecturerSlug);
  Iterable<Lecture> findByCategorySlugOrderByAddedDesc(String categorySlug);
  Iterable<Lecture> findByLecturerSlugOrderByAddedDesc(String lecturerSlug);
  List<Lecture> findByCategorySlugOrderByAddedDesc(String categorySlug, Pageable pageable);
  List<Lecture> findByLecturerSlugOrderByAddedDesc(String lecturerSlug, Pageable pageable);
  List<Lecture> findByOrderByAddedDesc(Pageable pageable);
  List<Lecture> findByOrderByRatingAverageDesc(Pageable pageable);
  
  @Query("SELECT l.name FROM Lecture l WHERE l.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
}

interface AbstractLectureRepositoryCustom<T extends Lecture> {
	List<T> search(String term, OrderBy orderBy, boolean decending);
	List<T> searchForCategory(String categorySlug, String term, OrderBy orderBy, boolean decending);
	List<T> searchForLecturer(String lecturerSlug, String term, OrderBy orderBy, boolean decending);
}