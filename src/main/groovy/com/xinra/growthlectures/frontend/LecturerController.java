package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.entity.Lecturer;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.LecturerService;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.List;
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
  
  @RequestMapping(Ui.URL_LECTURERS)
  public String lecturerList(Model model) {
    
    ArrayList<ContainerDto> allLecturers = new ArrayList<ContainerDto>();
    for(Lecturer l : lecturerService.getAllLecturers()) {
      ContainerDto newLecuturer = dtoFactory.createDto(ContainerDto.class);
      newLecuturer.setName(l.getName());
      newLecuturer.setSlug(l.getSlug());
      newLecuturer.setAmount(l.getLectures().size());
      allLecturers.add(newLecuturer);
    }
    
    ContainerDto firstMainLecturer = dtoFactory.createDto(ContainerDto.class);
    firstMainLecturer.setName("Erik Hofer");
    firstMainLecturer.setSlug("erik-hofer");
    firstMainLecturer.setAmount(124);
    
    ContainerDto secondMainLecturer = dtoFactory.createDto(ContainerDto.class);
    secondMainLecturer.setName("Toby Möller");
    secondMainLecturer.setSlug("toby-moeller");
    secondMainLecturer.setAmount(124);
    
    List<LectureSummaryDto> firstLecturerLectures = lectureService.getPopularLectures();
    while(firstLecturerLectures.size() > 3) {
      firstLecturerLectures.remove(3);      
    }
    model.addAttribute("firstMainLecturerLectures", firstLecturerLectures );
    
    List<LectureSummaryDto> secondLecturerLectures = lectureService.getPopularLectures();
    while(secondLecturerLectures.size() > 3) {
      secondLecturerLectures.remove(3);      
    }
    model.addAttribute("secondMainLecturerLectures", secondLecturerLectures );
    
    model.addAttribute("lecturerList", allLecturers );
    model.addAttribute("firstMainLecturer", firstMainLecturer);
    model.addAttribute("secondMainLecturer", secondMainLecturer);
    return "lecturers";
  }
  
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