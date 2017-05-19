package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.NamedDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Formatter {

  public static final String URL_CATEGORIES = "/lectures";
  public static final String URL_LECTURERS = "/lecturers";
  
  public String duration(Integer duration) {
    int hour = duration / 60 / 60;
    int minute = (duration / 60) % 60;
    int second = duration % 60;
        
    return String.format("%02d:%02d:%02d", hour, minute, second);
  }
  
  public String date(LocalDate date) {
    return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
  }
  
  public String lectureUrl(LectureSummaryDto dto) {
    return String.format("%s/%s/%s", URL_CATEGORIES, dto.getCategory().getSlug(), dto.getSlug());
  }
  
  public String categoryUrl(NamedDto dto) {
    return URL_CATEGORIES + "/" + dto.getSlug();
  }
  
  public String lecturerUrl(NamedDto dto) {
    return URL_LECTURERS + "/" + dto.getSlug();
  }
  
}