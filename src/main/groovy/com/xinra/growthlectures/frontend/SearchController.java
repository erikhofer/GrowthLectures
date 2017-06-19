package com.xinra.growthlectures.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SearchController extends GrowthlecturesController {
  
  @RequestMapping(Ui.URL_SEARCH)
  public String search(Model model) {
    
    addSearchModel(model, Ui.URL_SEARCH, searchService::search);
    
    return "search";
  }
  
}
