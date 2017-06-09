package com.xinra.growthlectures.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface AbstractCategoryRepository<T extends Category> 
    extends AbstractNamedEntityRepository<T> {

  @Query("SELECT c.name FROM Category c WHERE c.slug = :slug")
  String getNameBySlug(@Param("slug") String slug);
  
}
