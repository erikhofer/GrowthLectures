package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.ContainerDtoImpl;
import com.xinra.growthlectures.service.LectureServiceImpl;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.nucleus.service.DtoFactory;
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
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_INDEX)
  public String index(Model model) {
    
    // PopularLecures
    
    List<LectureSummaryDto> popularLectures = lectureServiceImpl.getPopularLectures();
    model.addAttribute("popularLectures", popularLectures );
    
    // Recent Lecturers
    List<LectureSummaryDto> recentLectures = lectureServiceImpl.getRecentLectures();
    model.addAttribute("recentLectures", recentLectures );
    
    // Popular Categories
    ArrayList<ContainerDto> popularCategories = new ArrayList<ContainerDto>();
    ContainerDto cat = dtoFactory.createDto(ContainerDto.class);
    cat.setName("Category One");
    cat.setSlug("catone");
    popularCategories.add(cat);
    model.addAttribute("popularCategories", popularCategories);
    
    // Pupular Lecuturers 
    ArrayList<ContainerDto> popularLecturers = new ArrayList<ContainerDto>();
    ContainerDto lec = dtoFactory.createDto(ContainerDto.class);
    lec.setName("Peter Lustig");
    lec.setSlug("peter-lustig");
    popularLecturers.add(lec);
    model.addAttribute("popularLecturers", popularLecturers);
    
    return "index";
  }

}
