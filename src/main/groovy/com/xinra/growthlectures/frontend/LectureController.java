package com.xinra.growthlectures.frontend;

import com.google.common.base.Preconditions;
import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.service.EditLectureDto;
import com.xinra.growthlectures.service.LectureDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.SlugNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
  
  private @Autowired LectureService lectureService;
  private @Autowired Ui ui;
  
  /**
   * Single lecture page.
   */
  @RequestMapping(PATH)
  public String lecturePage(Model model,
      @PathVariable("category") String category, 
      @PathVariable("lecture") String lecture) throws SlugNotFoundException {
    
    LectureDto lectureDto = lectureService.getBySlug(lecture, category, getUserId());
    
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
      @PathVariable(value = "lecture") String lecture) throws SlugNotFoundException {
    
    String lectureId = lectureService.getLectureId(lecture, category);
    return lectureService.getNote(lectureId, getUserId().get());
  }
  
  /**
   * REST controller for saving a lecture note. 
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/note", method = RequestMethod.POST)
  public String postNote(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture,
      @RequestBody String note) throws SlugNotFoundException {
    
    String lectureId = lectureService.getLectureId(lecture, category);
    return lectureService.saveNote(lectureId, getUserId().get(), note);
  }
  
  /**
   * REST controller for deleting a lecture note.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/note", method = RequestMethod.DELETE)
  public String deleteNote(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture) throws SlugNotFoundException {
    
    String lectureId = lectureService.getLectureId(lecture, category);
    lectureService.deleteNote(lectureId, getUserId().get());
    return null;
  }
  
  /**
   * REST controller for creating a new lecture.
   */
  @ResponseBody
  @RequestMapping(path = Ui.URL_CATEGORIES + "/{category}", method = RequestMethod.POST)
  public String addLectureInCategory(@PathVariable("category") String categorySlug, 
      EditLectureDto dto) throws InvalidDataException, SlugNotFoundException {
     
    List<String> errors = new ArrayList<String>();
    
    Util.normalize(dto, EditLectureDto.class);
      
    if (dto.getName() == null) {
      errors.add("Title is required!");
    }
    if (dto.getNewlecturer() == null) {
      errors.add("Lecturer is required!");
    }
    if (dto.getSlug() == null) {
      errors.add("Slug is required!");
    } else if (lectureService.doesSlugExists(dto.getSlug())) {
      errors.add("The slug already exists!");
    }
    if (dto.getDescription().length() > 4000) {
      errors.add("Description must not be longer than 4000 characters!");
    }
    Integer start = null;
    if (dto.getStart() == null) {
      errors.add("Start time is required!");
    } else {
      try {
        start = Util.parseDuration(dto.getStart());
      } catch (IllegalArgumentException iae) {
        errors.add("Start time invalid: " + iae.getMessage());
      }
    }
    Integer end = null;
    if (dto.getEnd() == null) {
      errors.add("End time required!");        
    } else {
      try {
        end = Util.parseDuration(dto.getEnd());
      } catch (IllegalArgumentException iae) {
        errors.add("Start time invalid: " + iae.getMessage());
      }
    }
    if (start != null && end != null && start >= end) {
      errors.add("Start time must be smaller than end time!");
    }
    
    if (!errors.isEmpty()) {
      throw new InvalidDataException(errors);
    }
    
    dto.setNewcategory(categorySlug); //ignore DTO value, category is set by path
    
    LectureSummaryDto newLecture = lectureService.createLecture(dto);
    return ui.lectureUrl(newLecture.getCategory().getSlug(), newLecture.getSlug()); 
  }
  
  /**
   * REST controller for saving a user rating.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/rating", method = RequestMethod.POST)
  public void saveRating(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture,
      @RequestBody int rating) throws SlugNotFoundException {
    
    lectureService.saveRating(lecture, category, getUserId().get(), rating);
  }
  
  /**
   * REST controller for deleting a user rating.
   */
  @ResponseBody
  @RequestMapping(path = PATH + "/rating", method = RequestMethod.DELETE)
  public void deleteRating(
      @PathVariable("category") String category,
      @PathVariable("lecture") String lecture) throws SlugNotFoundException {
    
    lectureService.deleteRating(lecture, category, getUserId().get());
  }
}
