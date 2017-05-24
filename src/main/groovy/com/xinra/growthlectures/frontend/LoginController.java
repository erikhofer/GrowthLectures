package com.xinra.growthlectures.frontend;

import com.xinra.growthlectures.service.EmailLoginDto;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class LoginController {
   
  @Autowired
  private DtoFactory dtoFactory;
  
  @RequestMapping(Ui.URL_LOGIN)
  public String loginPage(Model model) {
    
    model.addAttribute("loginUser", dtoFactory.createDto(EmailLoginDto.class));
    
    return "login";
  }
  
  @RequestMapping(value = Ui.URL_LOGIN, method = RequestMethod.POST)
  public String loginPagePost(Model model, EmailLoginDto user) {
       
    System.out.println(user.getEmail());
    System.out.println(user.getPassword());
    
    model.addAttribute("loginUser", user);
    model.addAttribute("errorMessage", "Incorrect username or password!");
    
    return "login";
  }

}
