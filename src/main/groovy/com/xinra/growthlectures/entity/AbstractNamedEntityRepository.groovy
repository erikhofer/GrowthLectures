package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractNamedEntityRepository<T extends NamedEntity> 
    extends AbstractEntityRepository<T> {

  T findBySlug(String slug);
  
}
