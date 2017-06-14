package com.xinra.growthlectures.service;

import com.google.javascript.jscomp.ConformanceRules.BanUnknownDirectThisPropsReferences
import com.xinra.growthlectures.entity.Category
import com.xinra.growthlectures.entity.CategoryRepository
import com.xinra.nucleus.service.DtoFactory
import groovy.transform.CompileStatic;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service;

@CompileStatic
@Service
class CategoryServiceImpl extends GrowthlecturesServiceImpl implements CategoryService {
    
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private CategoryRepository categoryRepo;
  
  public NamedDto getCategory(String slug) throws SlugNotFoundException {
    
    String name = categoryRepo.getNameBySlug(slug);
    if(name == null) {
      throw new SlugNotFoundException();
    }
   
    NamedDto dto = dtoFactory.createDto(NamedDto.class);
    dto.setSlug(slug);
    dto.setName(name);
    
    return dto;
  }
  
  public Collection<Category> getAllCategories() {
    return (Collection<Category>)categoryRepo.findAll();
  }
  
  public Category findCategory(String slug) throws SlugNotFoundException {
    Category cat = categoryRepo.findBySlug(slug);
    if(cat == null) {
      throw new SlugNotFoundException();
    }
    return cat;
  }
  
  public boolean doesExists(String slug) {
    Category cat = categoryRepo.findBySlug(slug);
    if(cat == null) return false;
    return true;
  }
  
  public Collection<NamedDto> getAllCategoriesAsNamedDto() {
    Collection<NamedDto> dtos = new ArrayList<NamedDto>();
    for (category in this.getAllCategories()) {
      NamedDto newCategory = dtoFactory.createDto(NamedDto.class);
      newCategory.setName(category.getName());
      newCategory.setSlug(category.getSlug());
      dtos.add(newCategory);
    }
    return dtos;    
  }
 
}
