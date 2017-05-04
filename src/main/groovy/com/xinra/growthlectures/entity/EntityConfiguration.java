package com.xinra.growthlectures.entity;

import com.xinra.nucleus.entity.DefaultEntityFactory;
import com.xinra.nucleus.entity.EntityFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfiguration {
  
  public EntityFactory entityFactory() {
    return new DefaultEntityFactory();
  }

}
