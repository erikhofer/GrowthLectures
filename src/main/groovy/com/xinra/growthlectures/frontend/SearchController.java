package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.SlugNotFoundException;
import com.xinra.nucleus.service.DtoFactory;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController {
  
//  @Autowired
//  SearchService searchService;
  
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_SEARCH)
  public String search(Model model) {
    

    model.addAttribute("results", Collections.emptyList());
    
    return "search";
  }
}
