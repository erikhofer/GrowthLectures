package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureServiceImpl;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.nucleus.service.DtoFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LectureController {
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private LectureServiceImpl lectureServiceImpl;
  
  @RequestMapping(Ui.URL_CATEGORIES + "/{CATEGORY}/{LECTURE}")
  public String lecturePage(Model model, @PathVariable(value="CATEGORY") String category, @PathVariable(value="LECTURE") String lecture) {
    
    System.out.println("Kategorie: "+category+" Lectuere: "+lecture);
    
    List<LectureSummaryDto> popularLectures = lectureServiceImpl.getPopularLectures();
    model.addAttribute("lecture", popularLectures.get(0) );
    
    return "lecture";
  }
  
}
