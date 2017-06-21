package com.xinra.growthlectures.service;

import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.entity.OrderBy;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl extends GrowthlecturesServiceImpl implements SearchService {
  
  @Autowired
  LectureService lectureService;
  
  @Autowired
  LectureRepository lectureRepo;
  
  @Autowired
  CategoryService categoryService;

  
  public List<LectureSummaryDto> search(String query, OrderBy orderBy, boolean decending) {
    
    return lectureRepo.search(query, orderBy, decending).stream()
        .map(lectureService::convertToSummaryDto)
        .collect(Collectors.toList());
  }
  
  public List<LectureSummaryDto> searchForCategory(String categorySlug, String query, 
      OrderBy orderBy, boolean decending) {
    
    return lectureRepo.searchForCategory(categorySlug, query, orderBy, decending).stream()
        .map(lectureService::convertToSummaryDto)
        .collect(Collectors.toList());
    
  }
  
  public List<LectureSummaryDto> searchForLecturer(String lecturerSlug, String query,
      OrderBy orderBy, boolean decending) {
    
    return lectureRepo.searchForLecturer(lecturerSlug, query, orderBy, decending).stream()
        .map(lectureService::convertToSummaryDto)
        .collect(Collectors.toList());
    
  }
  

}
