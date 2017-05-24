package com.xinra.growthlectures.frontend;

import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class LoginController {
   
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_LOGIN)
  public String index(Model model) {
       
    return "login";
  }

}
