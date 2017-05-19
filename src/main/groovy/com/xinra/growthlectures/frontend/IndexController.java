package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.service.ContainerDtoImpl;
import com.xinra.growthlectures.service.LectureServiceImpl;
import com.xinra.growthlectures.service.LectureSummaryDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexController {
  
  @Autowired
  LectureServiceImpl lectureServiceImpl;
  
  @RequestMapping("/")
  public String index(Model model) {
    
    // PopularLecures
    
    List<LectureSummaryDto> popularLectures = lectureServiceImpl.getPopularLectures();
    model.addAttribute("popularLectures", popularLectures );
    
    // Recent Lecturers
    List<LectureSummaryDto> recentLectures = lectureServiceImpl.getRecentLectures();
    model.addAttribute("recentLectures", recentLectures );
    
    // Popular Categories
    ArrayList<ContainerDtoImpl> popularCategories = new ArrayList<ContainerDtoImpl>();
    model.addAttribute("popularCategories", popularCategories);
    
    // Pupular Lecuturers 
    ArrayList<ContainerDtoImpl> popularLecturers = new ArrayList<ContainerDtoImpl>();        
    model.addAttribute("popularLecturers", popularLecturers);
    
    return "index";
  }

}
