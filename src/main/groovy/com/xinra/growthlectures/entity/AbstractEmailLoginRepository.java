package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.data.repository.query.Param;

public interface AbstractEmailLoginRepository<T extends EmailLogin> 
    extends AbstractEntityRepository<T> {

  EmailLogin findByEmailIgnoreCase(String email);
  
  //@Query("SELECT e FROM EmailLogin WHERE UPPER(e.email) = UPPER(:email)")
  //EmailLogin findByEmailIgnoreCaseEager(@Param("email") String email);
  
}
