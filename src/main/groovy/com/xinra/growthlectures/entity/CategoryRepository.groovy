package com.xinra.growthlectures.entity;

import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository;

@Repository
@CompileStatic
interface CategoryRepository extends AbstractCategoryRepository<Category> {}

@NoRepositoryBean
public interface AbstractCategoryRepository<T extends Category>
    extends AbstractNamedEntityRepository<T> {

  @Query("SELECT c.name FROM Category c WHERE c.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
}

