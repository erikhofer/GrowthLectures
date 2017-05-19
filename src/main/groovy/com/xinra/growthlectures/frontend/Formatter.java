package com.xinra.growthlectures.frontend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Formatter {

  public String duration(Integer duration) {
    int hour = duration / 60 / 60;
    int minute = (duration / 60) % 60;
    int second = duration % 60;
        
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
  
}