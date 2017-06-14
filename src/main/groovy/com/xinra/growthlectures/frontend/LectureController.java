package com.xinra.growthlectures.frontend;

import com.google.javascript.jscomp.GoogleCodingConvention;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.growthlectures.service.UserDto;
import com.xinra.nucleus.service.DtoFactory;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LectureController extends GrowthlecturesController {
  
  private static final String PATH = Ui.URL_CATEGORIES + "/{category}/{lecture}";
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private LectureService lectureService;
  
  @RequestMapping(PATH)
  public String lecturePage(Model model, @PathVariable(value="category") String category, @PathVariable(value="lecture") String lecture) {
    
    System.out.println("Kategorie: "+category+" Lectuere: "+lecture);
    
    List<LectureSummaryDto> popularLectures = lectureService.getPopularLectures();
    
    model.addAttribute("lecture", popularLectures.get(0));
    
    return "lecture";
  }
  
  /**
   * REST controller for retrieving a lecture note.
   */
  @ResponseBody
  @RequestMapping(PATH + "/note")
  public String getNote(
      @PathVariable(value = "category") String category,
      @PathVariable(value = "lecture") String lecture) {
    
    try {
      String lectureId = lectureService.getLectureId(lecture, category);
      return lectureService.getNote(lectureId, getUserDto().getPk().getId());
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
  }
  
  /**
   * REST controller for saving a lecture note.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/note", method = RequestMethod.POST)
  public String postNote(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture,
      @RequestParam("note") String note) {
    
    try {
      String lectureId = lectureService.getLectureId(lecture, category);
      return lectureService.saveNote(lectureId, getUserDto().getPk().getId(), note);
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
  }
}
