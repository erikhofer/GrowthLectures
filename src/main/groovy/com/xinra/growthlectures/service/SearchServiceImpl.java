package com.xinra.growthlectures.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl extends GrowthlecturesServiceImpl implements SearchService {
  
  @Autowired
  LectureService lectureService;
  
  public List<LectureSummaryDto> search(String query, OrderBy orderBy, boolean decending) {
    
    return lectureService.getPopularLectures();
    
  }
  
  public List<LectureSummaryDto> searchForCategory(String categorySlug, String query,
      OrderBy orderBy, boolean decending) throws SlugNotFoundException {
    
    return lectureService.getPopularLectures();
    
  }
  
  public List<LectureSummaryDto> searchForLecturer(String lecturerSlug, String query,
      OrderBy orderBy, boolean decending) throws SlugNotFoundException {
    
    return lectureService.getPopularLectures();
    
  }
  

}
