package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.NamedDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Ui {
  
  public static final String URL_INDEX = "/";
  public static final String URL_CATEGORIES = "/lectures";
  public static final String URL_LECTURERS = "/lecturers";
  public static final String URL_LOGIN = "/login";
  public static final String URL_SEARCH = "/search";
  public static final String URL_LOGOUT = "/logout";
  public static final String URL_REGISTER = "/register";
  
  public String lectureUrl(LectureSummaryDto dto) {
    return this.lectureUrl(dto.getCategory().getSlug(), dto.getSlug() );
  }
  
  public String lectureUrl(String categorySlug, String lectureSlug) {
    return String.format("%s/%s/%s", URL_CATEGORIES, categorySlug, lectureSlug);
  }
  
  public String categoryUrl(NamedDto dto) {
    return URL_CATEGORIES + "/" + dto.getSlug();
  }
  
  public String lecturerUrl(NamedDto dto) {
    return URL_LECTURERS + "/" + dto.getSlug();
  }

}
