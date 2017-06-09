package com.xinra.growthlectures.service;

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
}
