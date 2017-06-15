package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.service.CategoryService;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.LecturerService;
import com.xinra.growthlectures.service.NamedDto;
import com.xinra.growthlectures.service.NewLectureDto;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.Collection;
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
public class CategoryController {

  @Autowired
  DtoFactory dtoFactory;
  
  @Autowired
  LectureService lectureService;
  
  @Autowired
  CategoryService categoryService;
  
  @Autowired
  LecturerService lecturerService;
  
  @Autowired
  LectureRepository lectureRepo;
  
  @Autowired
  Ui ui;
  
  @RequestMapping(Ui.URL_CATEGORIES)
  public String categoryList(Model model) {
    

    ArrayList<ContainerDto> allCategories = new ArrayList<ContainerDto>();
 
    Collection<Category> categories = categoryService.getAllCategories();
    for(Category cat : categories) {
      ContainerDto newCat = dtoFactory.createDto(ContainerDto.class);
      newCat.setName(cat.getName());
      newCat.setSlug(cat.getSlug());
      newCat.setAmount(cat.getLectures().size());
     
      allCategories.add(newCat);
    }
    
    ContainerDto firstMainCat = dtoFactory.createDto(ContainerDto.class);
    firstMainCat.setName("Hauptkategorie 1");
    firstMainCat.setSlug("mainCatOne");
    firstMainCat.setAmount(124);
    
    ContainerDto secondMainCat = dtoFactory.createDto(ContainerDto.class);
    secondMainCat.setName("Hauptkategorie 2");
    secondMainCat.setSlug("mainCatTwo");
    secondMainCat.setAmount(124);
    
    List<LectureSummaryDto> firstCatLectures = lectureService.getPopularLectures();
    while(firstCatLectures.size() > 3) {
      firstCatLectures.remove(3);      
    }
    model.addAttribute("firstMainCatLectures", firstCatLectures );
    
    List<LectureSummaryDto> secondCatLectures = lectureService.getPopularLectures();
    while(secondCatLectures.size() > 3) {
      secondCatLectures.remove(3);      
    }
    model.addAttribute("secondMainCatLectures", secondCatLectures );
    
    model.addAttribute("categoryList", allCategories );
    model.addAttribute("firstMainCat", firstMainCat);
    model.addAttribute("secondMainCat", secondMainCat);
     
    return "categories";
  }
  
  @RequestMapping(Ui.URL_CATEGORIES + "/{SLUG}")
  public String category(Model model, @PathVariable("SLUG") String slug) throws SlugNotFoundException {
 
    NamedDto category = categoryService.getCategory(slug);
    List<LectureSummaryDto> lectures = lectureService.getPopularLectures();
   
    model.addAttribute("category", category);
    model.addAttribute("lectures", lectures );
    model.addAttribute("allCategories", categoryService.getAllCategories());
    model.addAttribute("allLecturers", lecturerService.getAllLecturers());
    model.addAttribute("newLecture", dtoFactory.createDto(NewLectureDto.class));
 
    return "category";
  }
  
  @ResponseBody
  @RequestMapping(value = Ui.URL_CATEGORIES + "/{SLUG}", method = RequestMethod.POST)
  public List<String> category(@PathVariable("SLUG") String categorySlug, NewLectureDto newLectureDto, HttpServletResponse response) throws SlugNotFoundException {
     
    List<String> responseList = new ArrayList<String>();
    
    String videoId = Util.normalize(newLectureDto.getVideoId());
    String title = Util.normalize(newLectureDto.getName());
    String slug = Util.normalize(newLectureDto.getSlug());
    String description = Util.normalize(newLectureDto.getDescription());
    String url = Util.normalize(newLectureDto.getLink());
    String category = Util.normalize(categorySlug);
    String lecturer = Util.normalize(newLectureDto.getNewlecturer());
    String platform = Util.normalize(newLectureDto.getPlatform());
    
    String start = Util.normalize(newLectureDto.getStart());
    String end = Util.normalize(newLectureDto.getEnd());
    
    
    if(videoId == null || url == null || platform == null) {
      responseList.add("Invalid Url - Please cancel and restart adding!");      
    } else {
      if(title == null) {
        responseList.add("The title is not valid!");
      }
      if(slug == null) {
        responseList.add("The slug is not valid!");
      } else {
        if(lectureService.doesSlugExists(slug)) {
          responseList.add("The slug already exists!");
        }
      }
      if(description.length() > 4000) {
        responseList.add("Description must be shorter than 4000 chars!");
      }
      if(category == null) {
        responseList.add("The selected category is not valid!");
      } else {
        try {
          categoryService.getCategory(category);
        } catch (SlugNotFoundException snfe) {
          throw new ResourceNotFoundException();
        }
      }
      if(lecturer == null) {
        responseList.add("The selected lecturer is not valid!");
      } else {
        if(!lecturerService.doesExists(newLectureDto.getNewlecturer())) {
          responseList.add("The selected lecturer does not exist!");
        }
      }    
      if(start == null) {
        responseList.add("Please enter a start time!");
      } else {
        if(Util.parseTime(start) == null) {
          responseList.add("The entered start time is not valid!");
        }
      }
      if(end == null) {
        responseList.add("Please enter an end time!");        
      } else {
        if(Util.parseTime(end) == null) {
          responseList.add("The entered end time is not valid!");
        }
      }
      if(start != null && end != null) {
        if(Util.parseTime(start) != null && Util.parseTime(end) != null) {
          if(Util.parseTime(start) >= Util.parseTime(end)) {
            responseList.add("The start time must be smaller than end time!");
          }
        }
      }
    }
    if(responseList.isEmpty()) {
      LectureSummaryDto newLecture = lectureService.createLecture(newLectureDto);
      response.setStatus( HttpServletResponse.SC_OK  );      
      responseList.add(ui.lectureUrl(newLecture.getCategory().getSlug(), newLecture.getSlug()));
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  
    return responseList;  
  }
  
  
  
}
