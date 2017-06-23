package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.LectureRepository;
import com.xinra.growthlectures.service.CategoryService;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.EditLectureDto;
import com.xinra.growthlectures.service.LectureService;
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
public class CategoryController extends GrowthlecturesController {

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
  
  /**
   * Lists all categories.
   */
  @RequestMapping(Ui.URL_CATEGORIES)
  public String categoryList(Model model) {
    
    ContainerDto recommendetCategory = categoryService.getRandomCategory();
    if (recommendetCategory != null) {
      model.addAttribute("recommendedCategory", recommendetCategory);
      model.addAttribute("recommendedCategoryLectures", 
          lectureService.getRecentLecturesByCategory(recommendetCategory.getSlug(), 6));
    }
    
    List<ContainerDto> allCategories = categoryService.getAllCategories();
    model.addAttribute("categoryList", allCategories); 
    
    model.addAttribute("newCategory", dtoFactory.createDto(NamedDto.class));
     
    return "categories";
  }
  
  /**
   * Single category page.
   */
  @RequestMapping(Ui.URL_CATEGORIES + "/{SLUG}")
  public String category(Model model,
      @PathVariable("SLUG") String slug) throws SlugNotFoundException {
 
    model.addAttribute("category", categoryService.getCategoryBySlug(slug));
    
    addSearchModel(model, slug, (query, orderBy, decending) -> {
      
      return searchService.searchForCategory(slug, query, orderBy, decending);
    });
    
    // New Lecture
    model.addAttribute("allCategories", categoryService.getAllCategories());
    model.addAttribute("allLecturers", lecturerService.getAllLecturers());
    model.addAttribute("newLecture", dtoFactory.createDto(EditLectureDto.class));
 
    return "category";
  }
  
  /**
   * REST controller for saving a new category.
   */
  @ResponseBody
  @RequestMapping(path = Ui.URL_CATEGORIES, method = RequestMethod.POST)
  public String addCategory(NamedDto categoryDto,
      HttpServletResponse response) throws InvalidDataException {
  
    List<String> errors = new ArrayList<>();
    
    String name = Util.normalize(categoryDto.getName());
    String slug = Util.normalize(categoryDto.getSlug());
    
    if (name == null) {
      errors.add("Invalid name!");
    }
    if (slug == null) {
      errors.add("Invalid slug!");
    } else if (categoryService.doesExists(slug)) {
      errors.add("Slug already exists!");
    }
    
    if (!errors.isEmpty()) {
      throw new InvalidDataException(errors);
    }
    
    categoryService.createCategory(categoryDto);
    return Ui.URL_CATEGORIES + "/" + slug;
  } 
  
}
