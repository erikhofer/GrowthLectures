package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;

public interface AbstractEmailLoginRepository<T extends EmailLogin> 
    extends AbstractEntityRepository<T> {

  EmailLogin findByEmailIgnoreCase(String email);
  
}
