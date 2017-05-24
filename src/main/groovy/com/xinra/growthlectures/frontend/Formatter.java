package com.xinra.growthlectures.frontend;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Formatter {
  
  private final DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public String duration(Integer duration) {
    int hour = duration / 60 / 60;
    int minute = (duration / 60) % 60;
    int second = duration % 60;
        
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
  
  public String date(LocalDate date) {
    return date.format(dateFormatter);
  }
  
}