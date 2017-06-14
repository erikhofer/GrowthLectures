package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.service.CategoryService;
import com.xinra.growthlectures.service.ContainerDto;
import com.xinra.growthlectures.service.LectureService;
import com.xinra.growthlectures.service.LectureSummaryDto;
import com.xinra.growthlectures.service.NamedDto;
import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CategoryController {

  @Autowired
  DtoFactory dtoFactory;
  
  @Autowired
  LectureService lectureService;
  
  @Autowired
  CategoryService categoryService;
  
  @Autowired
  SearchController searchController;
  
  @RequestMapping(Ui.URL_CATEGORIES)
  public String categoryList(Model model) {
    
    searchController.addSearchModel(model, Ui.URL_CATEGORIES, false);
    
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
    model.addAttribute("category", category);
    
    List<LectureSummaryDto> lectures = lectureService.getPopularLectures();
    model.addAttribute("lectures", lectures );
    
    return "category";
  }
  
}
