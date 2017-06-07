package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LecturerService;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LecturerController {
  
  @Autowired
  LecturerService lecturerService;
  
  @Autowired
  LectureService lectureService;
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_LECTURERS + "/{SLUG}")
  public String lecturer(Model model, @PathVariable("SLUG") String slug) {
    System.out.println(slug);
    try {
      model.addAttribute("lecturer", lecturerService.getLecturer(slug));
      model.addAttribute("lectures", lectureService.getPopularLectures());
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
    
    return "lecturer";
  }

}
