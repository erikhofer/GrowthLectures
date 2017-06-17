package com.xinra.growthlectures.service;

import com.google.common.collect.Streams;
import com.xinra.growthlectures.Util;
import com.xinra.growthlectures.entity.Category;
import com.xinra.growthlectures.entity.CategoryRepository;
import com.xinra.nucleus.service.DtoFactory;
import groovy.transform.CompileStatic;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@CompileStatic
@Service
class CategoryServiceImpl extends GrowthlecturesServiceImpl implements CategoryService {
    
  @Autowired
  private DtoFactory dtoFactory;
  
  @Autowired
  private CategoryRepository categoryRepo;
  
  public ContainerDto getRandomCategory() {
    Long categoriesCount = categoryRepo.count();
    int randomIndex = (int)(Math.random() * categoriesCount);
    Page<Category> categoryList = categoryRepo.findAll(new PageRequest(randomIndex,1));
    Category randomCategory = null;
    for (Category l : categoryList) {
      randomCategory = l;
    }
    return convertCategory(randomCategory);
  }
    
  public ContainerDto getCategoryBySlug(String slug) throws SlugNotFoundException {
    
    Category category = categoryRepo.findOneBySlug(slug);
    if (category == null) {
      throw new SlugNotFoundException(slug);
    }
    
    return convertCategory(category);
  }
  
  public List<ContainerDto> getAllCategories() {
    return Streams.stream(categoryRepo.findAll())
            .map(this::convertCategory)
            .collect(Collectors.toList());
  }
  
  public boolean doesExists(String slug) {
    Category cat = categoryRepo.findBySlug(slug);
    if (cat == null) {
      return false;
    }
    return true;
  }
  
  public NamedDto createCategory(NamedDto newCategoryDto) {
    Category newCategory = new Category();
    
    String name = Util.normalize(newCategoryDto.getName());
    String slug = Util.normalize(newCategoryDto.getSlug());
    newCategory.setName(name);
    newCategory.setSlug(slug);
    categoryRepo.save(newCategory);
    
    return newCategoryDto;
    
  }
  
  private ContainerDto convertCategory(Category c) {
    
    if (c == null) {
      return null;
    }
    
    ContainerDto returnDto = dtoFactory.createDto(ContainerDto.class);
    returnDto.setName(c.getName());
    returnDto.setSlug(c.getSlug());
    returnDto.setAmount(c.getLectures().size());
    
    return returnDto;
    
  }
 
}
