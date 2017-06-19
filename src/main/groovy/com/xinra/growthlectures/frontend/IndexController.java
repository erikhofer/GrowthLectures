package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.CategoryService;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.LectureServiceImpl;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.LecturerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {
  
  @Autowired
  LectureServiceImpl lectureServiceImpl;
  
  @Autowired
  private CategoryService categoryService;
  
  @Autowired 
  LecturerService lecturerService;
  
  /**
   * Index page.
   */
  @RequestMapping(Ui.URL_INDEX)
  public String index(Model model) {
    
    // PopularLectures
    List<LectureSummaryDto> popularLectures = lectureServiceImpl.getPopularLectures(4);
    model.addAttribute("popularLectures", popularLectures);
    
    // Recent Lecturers
    List<LectureSummaryDto> recentLectures = lectureServiceImpl.getRecentLectures(4);
    model.addAttribute("recentLectures", recentLectures);
    
    
    // Popular Categories
    List<ContainerDto> popularCategories = categoryService.getPopularCategories(5);
    model.addAttribute("popularCategories", popularCategories);
    
    // Pupular Lecuturers 
    List<ContainerDto> popularLecturers = lecturerService.getPopularLecturers(5);
    model.addAttribute("popularLecturers", popularLecturers);
    
    return "index";
  }

}
