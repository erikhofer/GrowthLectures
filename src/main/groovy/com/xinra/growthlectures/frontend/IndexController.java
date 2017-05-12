package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.Lecture;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.ContainerDtoImpl;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
  
  @RequestMapping("/")
  public String index(Model model) {
   
    // PopularLecures
    ArrayList<Lecture> popularLectures = new ArrayList<Lecture>();
    model.addAttribute("popularLectures", popularLectures );
    
    // Recent Lecturers
    ArrayList<Lecture> recentLectures = new ArrayList<Lecture>();
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
