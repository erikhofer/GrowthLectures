package com.xinra.growthlectures.service;

import com.xinra.nucleus.service.CoercionDtoFactory;
import com.xinra.nucleus.service.DtoFactory;
import com.xinra.nucleus.service.ImplDtoFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

  @Bean
  public DtoFactory dtoFactory() {
    return new CoercionDtoFactory(new ImplDtoFactory());
  }

}
