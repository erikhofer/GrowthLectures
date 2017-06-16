package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.LecturerService;
import com.xinra.growthlectures.service.NamedDto;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LecturerController {
  
  @Autowired
  LecturerService lecturerService;
  
  @Autowired
  LectureService lectureService;
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_LECTURERS)
  public String lecturerList(Model model) {
    
    ContainerDto recommendedLecturer = lecturerService.getRandomLecturer();
    if (recommendedLecturer == null) {
      model.addAttribute("recommendedLecturer", null);    
    } else {
      model.addAttribute("recommendedLecturer", recommendedLecturer);
      model.addAttribute("recommendedLecturerLectures", 
          lectureService.getRecentLecturesByLecturer(recommendedLecturer.getSlug(), 6));
    }
    
    List<ContainerDto> allLecturers = lecturerService.getAllLecturers();
    if (allLecturers == null || allLecturers.size() == 0) {
      model.addAttribute("lecturerList", null);  
    } else {
      model.addAttribute("lecturerList", allLecturers);   
    }

    model.addAttribute("newLecturer", dtoFactory.createDto(NamedDto.class));   
    
    return "lecturers";
  }
  
  @RequestMapping(Ui.URL_LECTURERS + "/{SLUG}")
  public String lecturer(Model model, @PathVariable("SLUG") String slug) {
    
    try {
      model.addAttribute("lecturer", lecturerService.getLecturerBySlug(slug));
    } catch (SlugNotFoundException snfe) {
      throw new ResourceNotFoundException();
    }

    model.addAttribute("lectures", lectureService.getRecentLecturesByLecturer(slug));
    
    return "lecturer";
  }
  
  @ResponseBody
  @RequestMapping(value = Ui.URL_LECTURERS, method = RequestMethod.POST)
  public List<String> addLecturer(NamedDto newLecturerDto, 
                                  HttpServletResponse response) {
  
    List<String> responseList = new ArrayList<String>();
    String name = Util.normalize(newLecturerDto.getName());
    String slug = Util.normalize(newLecturerDto.getSlug());
    if (name == null) {
      responseList.add("Invalid name!");
    }
    if (slug == null) {
      responseList.add("Invalid slug!");
    } else {
      if (lecturerService.doesExists(slug)) {
        responseList.add("Slug alredy exists!");
      }
    }
    
    if (responseList.isEmpty()) {
      lecturerService.createLecturer(newLecturerDto);
      response.setStatus(HttpServletResponse.SC_OK);
      responseList.add(Ui.URL_LECTURERS + "/" + slug);
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    
    return responseList;
  }

}
