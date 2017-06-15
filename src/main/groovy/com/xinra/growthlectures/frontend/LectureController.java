package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.LectureDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.SlugNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LectureController extends GrowthlecturesController {
  
  private static final String PATH = Ui.URL_CATEGORIES + "/{category}/{lecture}";
  
  @Autowired
  private LectureService lectureService;
  
  /**
   * Single lecture page.
   */
  @RequestMapping(PATH)
  public String lecturePage(Model model,
      @PathVariable("category") String category, 
      @PathVariable("lecture") String lecture) {
    
    LectureDto lectureDto;
    try {
      lectureDto = lectureService
          .getBySlug(lecture, category, getUserId());
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
    
    model.addAttribute("lecture", lectureDto);
    return "lecture";
  }
  
  /**
   * REST controller for retrieving a lecture note.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/note", method = RequestMethod.GET)
  public String getNote(
      @PathVariable(value = "category") String category,
      @PathVariable(value = "lecture") String lecture) {
    
    try {
      String lectureId = lectureService.getLectureId(lecture, category);
      return lectureService.getNote(lectureId, getUserId());
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
      @RequestBody String note) {
    
    try {
      String lectureId = lectureService.getLectureId(lecture, category);
      return lectureService.saveNote(lectureId, getUserId(), note);
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
  }
  
  /**
   * REST controller for saving a lecture note.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/note", method = RequestMethod.DELETE)
  public String deleteNote(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture) {
    
    try {
      String lectureId = lectureService.getLectureId(lecture, category);
      lectureService.deleteNote(lectureId, getUserId());
      return null;
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }
  }
}
