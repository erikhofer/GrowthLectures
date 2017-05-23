package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;

public interface EmailLoginRepository extends AbstractEntityRepository<EmailLogin> {
  
  EmailLogin findByEmailIgnoreCase(String email);

}
