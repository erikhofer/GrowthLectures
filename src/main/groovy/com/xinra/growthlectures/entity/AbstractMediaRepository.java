package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractMediaRepository<T extends Media>
  extends AbstractEntityRepository<T>{


}
